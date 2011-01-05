package ua.orion.core.services;

import java.io.Serializable;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.*;
import org.slf4j.Logger;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.MetaEntity;
import ua.orion.core.utils.Defense;
import ua.orion.core.validation.UniqueConstraintValidator;

/**
 *
 * @author sl
 */
//@Scope(ScopeConstants.PERTHREAD)
public class EntityServiceImpl implements EntityService {

    private final EntityManager em;
    private final Metamodel metamodel;
    private final PropertyAccess propertyAccess;
    private final UniqueConstraintValidator validator;
    private final Logger logger;
    private final TypeCoercer typeCoercer;
    private final Map<Class<?>, MetaEntity> metaEntityByEntityClass = new HashMap<Class<?>, MetaEntity>();

    public EntityServiceImpl(EntityManager entityManager,
            Logger logger, PropertyAccess propertyAccess,
            TypeCoercer typeCoercer) {
        this.propertyAccess = propertyAccess;
        this.em = entityManager;
        this.logger = logger;
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
    public <T> T persistOrGet(T entity) {
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
    public <T> T findByUserPresentableOrPrimaryKey(Class<T> type, String name) {
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
    public String getUserPresentableOrPrimaryKey(Object entity) {
        Defense.notNull(entity, "entity");
        if (getMetaEntity(entity.getClass()).supportUserPresentable()) {
            return (String) propertyAccess.get(entity, getMetaEntity(entity.getClass()).getUserPresentableAttributeName());
        } else {
            return typeCoercer.coerce(getPrimaryKey(entity), String.class);
        }
    }

    @Override
    public <T> T findByUKey(Class<T> type, String uKey) {
        throw new UnsupportedOperationException("Not supported yet.");
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
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean supportUserPresentable() {
            if (supportUserPresentable == null) {
                if (type.isAnnotationPresent(UserPresentable.class)) {
                    userPresentableAttributeName = type.getAnnotation(UserPresentable.class).value();
                }
            }
            return supportUserPresentable;
        }

        @Override
        public String getLabel(Messages messages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getLabel(Locale locale) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getPropertyLabel(String propertyName, Messages messages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getPropertyLabel(String propertyName, Locale locale) {
            throw new UnsupportedOperationException("Not supported yet.");
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
    }
}
