package ua.orion.core.services;

import java.io.Serializable;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.services.*;
import org.slf4j.Logger;
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
    public <T> T persistOrGet(Class<T> type, T entity) {
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
    public <T> T findByUserPresentable(Class<T> type, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getUserPresentable(Object entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T findByUKey(Class<T> type, String uKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getUKey(Object entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setUKey(Object entity, String uKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPrimaryKey(Object entity, Serializable primaryKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Serializable getPrimaryKey(Object entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean supportUKey(Class<?> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLabel(Class<?> type, Messages messages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLabel(Class<?> type, Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPropertyLabel(Class<?> type, String propertyName, Messages messages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPropertyLabel(Class<?> type, String propertyName, Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
