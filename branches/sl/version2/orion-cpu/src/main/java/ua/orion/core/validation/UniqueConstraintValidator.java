package ua.orion.core.validation;

import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import javax.validation.*;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;

/**
 * Ограничения: Уникальный атрибут сущности и его геттер должен соответствовать соглашению по именованию,
 * т.е. у атрибута name геттер getName(). В случае, например если атрибут называется internalName, а геттер
 * getName() проверка уникальности этого атрибута может быть выполнена неверна.
 * Инициализация валидатора делается либо установкой статических членов
 * ENTITY_MANAGER_FACTORY и PROPERTY_ACCESS, либо с помощью фабрики
 * UniqueConstraintValidatorFactory
 * @author sl
 */
public class UniqueConstraintValidator implements ConstraintValidator<Unique, Object> {

    public static final String MESSAGE = "{javax.validation.constraints.Unique.message}";
    private EntityManager em = null;
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = null;
    private static PropertyAccess PROPERTY_ACCESS = null;

    public static void setENTITY_MANAGER_FACTORY(EntityManagerFactory entityManagerFactory) {
        UniqueConstraintValidator.ENTITY_MANAGER_FACTORY = entityManagerFactory;
    }

    public static void setPROPERTY_ACCESS(PropertyAccess pAccess) {
        UniqueConstraintValidator.PROPERTY_ACCESS = pAccess;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean isValid = getPersistentUniqueObject(object) == null;
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MESSAGE).addConstraintViolation();
        }
        return isValid;
    }

    /**
     *
     * @param entity
     * @return персистентную сущность, с которой конфлигтует ограничение уникальности
     * или null если конфликта нет.
     */
    public <T> T getPersistentUniqueObject(T entity) {
        if (em == null && ENTITY_MANAGER_FACTORY!=null) {
            em = ENTITY_MANAGER_FACTORY.createEntityManager();
        }
        if (entity == null || em == null || PROPERTY_ACCESS == null
                || entity.getClass().getAnnotation(Entity.class) == null) {
            return null;
        }
        Metamodel metamodel = em.getMetamodel();
        EntityType entityType = null;
        try {
            entityType = metamodel.entity(entity.getClass());
        } catch (IllegalArgumentException ex) {
            return null;
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        ClassPropertyAdapter cpa = PROPERTY_ACCESS.getAdapter(entity);

        //Process Singular Attributes
        CriteriaQuery<T> query = cb.createQuery((Class<T>)entity.getClass());
        Root<T> root = query.from(entityType);
        Set<SingularAttribute> singularAttributes = entityType.getSingularAttributes();
        if (singularAttributes.size() > 0) {
            List<Predicate> whereExprs = new ArrayList();
            for (SingularAttribute a : singularAttributes) {
                PropertyAdapter adapter = cpa.getPropertyAdapter(a.getName());
                Object value = adapter.get(entity);
                if (value != null) {
                    Class<?> entityClass = entity.getClass();
                    boolean isUniqueCalculated = false;
                    boolean isUnique = false;
                    do {
                        if (!a.isAssociation()) {
                            //Check AttributeOverride Annotation
                            if (!isUniqueCalculated) {
                                AttributeOverrides attributeOverrides = entityClass.getAnnotation(AttributeOverrides.class);
                                if (attributeOverrides != null) {
                                    for (AttributeOverride attributeOverride : attributeOverrides.value()) {
                                        if (attributeOverride != null && a.getName().equals(attributeOverride.name())) {
                                            isUnique = attributeOverride.column().unique();
                                            isUniqueCalculated = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            //Check AttributeOverride Annotation
                            if (!isUniqueCalculated) {
                                AttributeOverride attributeOverride = entityClass.getAnnotation(AttributeOverride.class);
                                if (attributeOverride != null && a.getName().equals(attributeOverride.name())) {
                                    isUnique = attributeOverride.column().unique();
                                    isUniqueCalculated = true;
                                }
                            }
                            //Check Column Annotation for Field
                            if (!isUniqueCalculated) {
                                Column column = null;
                                try {
                                    column = entityClass.getDeclaredField(a.getName()).getAnnotation(Column.class);
                                } catch (Exception ex) {
                                }
                                if (column != null) {
                                    isUnique = column.unique();
                                    isUniqueCalculated = true;
                                } else {
                                    //Check Column Annotation for getter
                                    column = adapter.getReadMethod().getAnnotation(Column.class);
                                    if (column != null) {
                                        isUnique = column.unique();
                                        isUniqueCalculated = true;
                                    }
                                }
                            }
                        } else {
                            //Check AssociationOverride Annotation
                            if (!isUniqueCalculated) {
                                AssociationOverrides associationOverrides = entityClass.getAnnotation(AssociationOverrides.class);
                                if (associationOverrides != null) {
                                    for (AssociationOverride associationOverride : associationOverrides.value()) {
                                        if (associationOverride != null && a.getName().equals(associationOverride.name())
                                                && associationOverride.joinColumns().length > 0) {
                                            isUnique = associationOverride.joinColumns()[0].unique();
                                            isUniqueCalculated = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            //Check AssociationOverride Annotation
                            if (!isUniqueCalculated) {
                                AssociationOverride associationOverride = entityClass.getAnnotation(AssociationOverride.class);
                                if (associationOverride != null && a.getName().equals(associationOverride.name())
                                        && associationOverride.joinColumns().length > 0) {
                                    isUnique = associationOverride.joinColumns()[0].unique();
                                    isUniqueCalculated = true;
                                }
                            }
                            //Check JoinColumn Annotation for Field
                            if (!isUniqueCalculated) {
                                JoinColumn joinColumn = null;
                                try {
                                    joinColumn = entityClass.getDeclaredField(a.getName()).getAnnotation(JoinColumn.class);
                                } catch (Exception ex) {
                                }
                                if (joinColumn != null) {
                                    isUnique = joinColumn.unique();
                                    isUniqueCalculated = true;
                                } else {
                                    //Check JoinColumn Annotation for getter
                                    joinColumn = adapter.getReadMethod().getAnnotation(JoinColumn.class);
                                    if (joinColumn != null) {
                                        isUnique = joinColumn.unique();
                                        isUniqueCalculated = true;
                                    }
                                }
                            }
                        }
                        entityClass = entityClass.getSuperclass();
                    } while (!isUniqueCalculated &&
                            (entityClass.isAnnotationPresent(Entity.class) ||
                            entityClass.isAnnotationPresent(MappedSuperclass.class)));
                    if (isUniqueCalculated && isUnique) {
                        whereExprs.add(cb.equal(root.get(a), value));
                    }
                }
            }
            if (whereExprs.size() > 0) {
                query.where(cb.or(whereExprs.toArray(new Predicate[whereExprs.size()])));
                List<T> list = em.createQuery(query).getResultList();
                if (!list.isEmpty()) {
                    return list.get(0);
                }
            }
        }

        //Process UniqueConstraint annotation
        query = cb.createQuery((Class<T>)entity.getClass());
        root = query.from(entityType);
        if (entity.getClass().isAnnotationPresent(Table.class)) {
            Table annotation = entity.getClass().getAnnotation(Table.class);
            if (annotation.uniqueConstraints() != null) {
                for (UniqueConstraint uniqueConstraint : annotation.uniqueConstraints()) {
                    List<Predicate> whereExprs = new ArrayList();
                    for (String uniqueColumn : uniqueConstraint.columnNames()) {
                        Object value = cpa.get(entity, uniqueColumn);
                        if (value != null) {
                            whereExprs.add(cb.equal(root.get(uniqueColumn), value));
                        } else {
                            //if at least one attribute is null, then unique check is passed
                            whereExprs.clear();
                            break;
                        }
                    }
                    if (whereExprs.size() > 0) {
                        query.where(whereExprs.toArray(new Predicate[whereExprs.size()]));
                        List<T> list = em.createQuery(query).getResultList();
                        if (!list.isEmpty()) {
                            return list.get(0);
                        }
                    }
                }
            }
        }
        return null;
    }
}
