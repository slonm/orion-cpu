package ua.orion.core.services;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.*;
import ua.orion.core.InheritedAnnotationProvider;
import ua.orion.persistence.annotations.UniqueKey;
import ua.orion.persistence.annotations.UserPresentable;
import ua.orion.persistence.MetaEntity;
import ua.orion.core.utils.Defense;
import ua.orion.core.validation.UniqueConstraintValidator;

/**
 *
 * @author sl
 */
public class EntityServiceImpl implements EntityService {

    private final EntityManager em;
    private final Metamodel metamodel;
    private final PropertyAccess propertyAccess;
    private final InheritedAnnotationProviderSource aProviderSource;
    private final ApplicationMessagesSource applicationMessagesSource;
    private final UniqueConstraintValidator validator;
    private final TypeCoercer typeCoercer;
    private final Map<Class<?>, MetaEntity> metaEntityByEntityClass = new HashMap<Class<?>, MetaEntity>();

    public EntityServiceImpl(EntityManager entityManager,
            PropertyAccess propertyAccess,
            TypeCoercer typeCoercer,
            ApplicationMessagesSource applicationMessagesSource,
            InheritedAnnotationProviderSource aProviderSource) {
        this.aProviderSource = aProviderSource;
        this.applicationMessagesSource = applicationMessagesSource;
        this.propertyAccess = propertyAccess;
        this.em = entityManager;
        this.typeCoercer = typeCoercer;
        metamodel = em.getMetamodel();
        validator = new UniqueConstraintValidator();
        validator.setEntityManager(em);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public <T> T findUniqueOrPersist(T entity) {
        T persistentEntity = validator.getPersistentUniqueObject(entity);
        if (persistentEntity == null) {
            em.persist(entity);
            return entity;
        } else {
            return persistentEntity;
        }
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        Defense.notNull(type, "type");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);
        try {
            query.where(cb.equal(root.get("name"), name));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Entity " + type.getName() + " not contains 'name' atribute");
        }
        List<T> list = em.createQuery(query).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void persist(Object entity) {
        em.persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return em.merge(entity);
    }

    @Override
    public void remove(Object entity) {
        em.remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Serializable primaryKey) {
        return em.find(entityClass, typeCoercer.coerce(primaryKey,
                metamodel.entity(entityClass).getIdType().getJavaType()));
    }

    @Override
    public <T> T find(Class<T> entityClass, Serializable primaryKey, LockModeType lockMode) {
        return em.find(entityClass, typeCoercer.coerce(primaryKey,
                metamodel.entity(entityClass).getIdType().getJavaType()), lockMode);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Serializable primaryKey) {
        return em.getReference(entityClass, typeCoercer.coerce(primaryKey,
                metamodel.entity(entityClass).getIdType().getJavaType()));
    }

    @Override
    public void setPrimaryKey(Object entity, Serializable primaryKey) {
        EntityType<?> eType = metamodel.entity(entity.getClass());
        Class<?> pkFieldType = eType.getIdType().getJavaType();
        Object key = typeCoercer.coerce(primaryKey, pkFieldType);
        propertyAccess.set(entity, eType.getId(pkFieldType).getName(), key);
    }

    @Override
    public Serializable getPrimaryKey(Object entity) {
        EntityType<?> eType = metamodel.entity(entity.getClass());
        Class<?> pkFieldType = eType.getIdType().getJavaType();
        return (Serializable) propertyAccess.get(entity, eType.getId(pkFieldType).getName());
    }

    @Override
    public synchronized MetaEntity getMetaEntity(Class<?> entityClass) {
        if (!metaEntityByEntityClass.containsKey(entityClass)) {
            metaEntityByEntityClass.put(entityClass, new MetaEntityImpl(entityClass));
        }
        return metaEntityByEntityClass.get(entityClass);
    }

    @Override
    public <T> T findByUserPresentable(Class<T> type, String name) {
        if (getMetaEntity(type).supportUserPresentable()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(type);
            Root<T> root = query.from(type);
            query.where(cb.equal(root.get(getMetaEntity(type).getUserPresentableAttributeName()), name));
            try {
                return em.createQuery(query).getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        } else {
            return find(type, name);
        }
    }

    @Override
    public String getUserPresentable(Object entity) {
        Defense.notNull(entity, "entity");
        if (getMetaEntity(entity.getClass()).supportUserPresentable()) {
            return (String) propertyAccess.get(entity, getMetaEntity(entity.getClass()).getUserPresentableAttributeName());
        } else {
            return typeCoercer.coerce(getPrimaryKey(entity), String.class);
        }
    }

    @Override
    public <T> T findByUKey(Class<T> type, String uKey) {
        if (getMetaEntity(type).supportUKey()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(type);
            Root<T> root = query.from(type);
            query.where(cb.equal(root.get(getMetaEntity(type).getUKeyAttributeName()), uKey));
            try {
                return em.createQuery(query).getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        }
        throw new IllegalArgumentException("UKey not supported by " + type.getName());
    }

    @Override
    public String getUKey(Object entity) {
        Defense.notNull(entity, "entity");
        if (getMetaEntity(entity.getClass()).supportUKey()) {
            return (String) propertyAccess.get(entity, getMetaEntity(entity.getClass()).getUKeyAttributeName());
        }
        throw new IllegalArgumentException("UKey not supported by " + entity.getClass().getName());
    }

    @Override
    public void setUKey(Object entity, String uKey) {
        Defense.notNull(entity, "entity");
        if (getMetaEntity(entity.getClass()).supportUKey()) {
            propertyAccess.set(entity, getMetaEntity(entity.getClass()).getUKeyAttributeName(), uKey);
        }
        throw new IllegalArgumentException("UKey not supported by " + entity.getClass().getName());
    }

    @Override
    public Object getVersion(Object entity) {
        //TODO Непонятно как это сделать
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearCache() {
        metaEntityByEntityClass.clear();
    }

    @Override
    public MetaEntity getMetaEntity(String entityName) {
        Defense.notBlank(entityName, "entityName");
        for(EntityType<?> et: metamodel.getEntities()){
            //используем et.getJavaType().getSimpleName() вместо et.getName()
            //так как Hibernate в этом случае возвращает каноническое имя (с пакетом)
            if(et.getJavaType().getSimpleName().equalsIgnoreCase(entityName)){
                return getMetaEntity(et.getJavaType());
            }
        }
        throw new IllegalArgumentException("Not managed entity " + entityName);
    }

    @Override
    public <T> T newInstance(Class<T> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        }
        return null;
    }

    class MetaEntityImpl implements MetaEntity {

        private final Class<?> type;
        private Boolean supportUKey = null;
        private Boolean supportUserPresentable = null;
        private String userPresentableAttributeName = null;
        private String uKeyAttributeName = null;

        public MetaEntityImpl(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean supportUKey() {
            if (supportUKey == null) {
                supportUKey = false;
                InheritedAnnotationProvider classProvider = aProviderSource.getClassProviderWithInterfaces(type);
                UniqueKey classAnn = classProvider.getAnnotation(UniqueKey.class);
                if (classAnn != null) {
                    uKeyAttributeName = classAnn.value();
                    supportUKey = true;
                }
                ClassPropertyAdapter cpa = propertyAccess.getAdapter(type);
                for (String name : cpa.getPropertyNames()) {
                    InheritedAnnotationProvider propertyProvider = aProviderSource.getPropertyProviderWithInterfaces(type, name);
                    UniqueKey propAnn = propertyProvider.getAnnotation(UniqueKey.class);
                    if (propAnn != null && (classAnn == null || classProvider.getDeclarationBeanType(UniqueKey.class).isAssignableFrom(propertyProvider.getDeclarationBeanType(UniqueKey.class)))) {
                        uKeyAttributeName = name;
                        supportUKey = true;
                    }
                }
            }
            return supportUKey;
        }

        @Override
        public boolean supportUserPresentable() {
            if (supportUserPresentable == null) {
                supportUserPresentable = false;
                InheritedAnnotationProvider classProvider = aProviderSource.getClassProviderWithInterfaces(type);
                UserPresentable classAnn = classProvider.getAnnotation(UserPresentable.class);
                if (classAnn != null) {
                    userPresentableAttributeName = classAnn.value();
                    supportUserPresentable = true;
                }
                ClassPropertyAdapter cpa = propertyAccess.getAdapter(type);
                for (String name : cpa.getPropertyNames()) {
                    InheritedAnnotationProvider propertyProvider = aProviderSource.getPropertyProviderWithInterfaces(type, name);
                    UserPresentable propAnn = propertyProvider.getAnnotation(UserPresentable.class);
                    if (propAnn != null && (classAnn == null || classProvider.getDeclarationBeanType(UserPresentable.class).isAssignableFrom(propertyProvider.getDeclarationBeanType(UserPresentable.class)))) {
                        userPresentableAttributeName = name;
                        supportUserPresentable = true;
                    }
                }
            }
            return supportUserPresentable;
        }

        @Override
        public String getUserPresentableAttributeName() {
            supportUserPresentable();
            return userPresentableAttributeName;
        }

        @Override
        public String getUKeyAttributeName() {
            supportUKey();
            return uKeyAttributeName;
        }

        @Override
        public String getLabel(Messages messages) {
            return messages.get("entity." + type.getSimpleName());
        }

        @Override
        public String getLabel(Locale locale) {
            return getLabel(applicationMessagesSource.getMessages(Locale.getDefault()));
        }

        @Override
        public String getPropertyLabel(String propertyName, Messages messages) {
            return messages.get("property." + type.getSimpleName() + "." + propertyName);
        }

        @Override
        public String getPropertyLabel(String propertyName, Locale locale) {
            return getPropertyLabel(propertyName, applicationMessagesSource.getMessages(Locale.getDefault()));
        }

        @Override
        public Class<?> getEntityClass() {
            return type;
        }

        @Override
        public String getEntityName() {
            return type.getSimpleName();
        }
    }
}
