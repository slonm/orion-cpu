package ua.orion.core.validation;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import javax.validation.*;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import java.util.*;

/**
 * Ограничения: Уникальный атрибут сущности и его геттер должен соответствовать соглашению по именованию,
 * т.е. у атрибута name геттер getName(). В случае, например если атрибут называется internalName, а геттер
 * getName() проверка уникальности этого атрибута может быть выполнена неверна.
 * Инициализация валидатора делается либо установкой статических членов
 * ENTITY_MANAGER_FACTORY и PROPERTY_ACCESS, либо с помощью фабрики
 * UniqueConstraintValidatorFactory.
 * При явной передаче EntityManager валидация происходит в текущей транзакции, при этом доступны незафиксированные 
 * изменения, но при этом в кеш EntityManager попадают прочитанные во время поиска сущности, кроме того EntityManager
 * может быть переопределенным подсистемой безопасности и не "видеть" некоторые сущности, которые реально есть в хранилище,
 * но при попытке сохранения сущности нарушающей условия уникальности хранилище все равно будет выбрасывать исключение.
 * @author sl
 */
public class UniqueConstraintValidator implements ConstraintValidator<Unique, Object> {

    public static final String MESSAGE = "{javax.validation.constraints.Unique.message}";
    private EntityManager em = null;
    private CriteriaBuilder cb;
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
        cb = em.getCriteriaBuilder();
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
        if (em == null && ENTITY_MANAGER_FACTORY != null) {
            em = ENTITY_MANAGER_FACTORY.createEntityManager();
        }
        if (entity == null || em == null || PROPERTY_ACCESS == null
                || entity.getClass().getAnnotation(Entity.class) == null) {
            return null;
        }
        Metamodel metamodel = em.getMetamodel();
        EntityType entityType = null;
        Class<T> entityClass = (Class<T>) entity.getClass();
        try {
            entityType = metamodel.entity(entityClass);
        } catch (IllegalArgumentException ex) {
            return null;
        }
        ClassPropertyAdapter cpa = PROPERTY_ACCESS.getAdapter(entity);

        //Process Singular Attributes
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityType);
        Set<SingularAttribute> singularAttributes = entityType.getSingularAttributes();
        if (singularAttributes.size() > 0) {
            List<Predicate> whereExprs = new ArrayList();
            for (SingularAttribute a : singularAttributes) {
                PropertyAdapter adapter = cpa.getPropertyAdapter(a.getName());
                Object value = adapter.get(entity);
                if (value != null) {
                    Class<?> entitySuperClass = entityClass;
                    boolean isUniqueCalculated = false;
                    boolean isUnique = false;
                    do {
                        if (!a.isAssociation()) {
                            //Check AttributeOverride Annotation
                            if (!isUniqueCalculated) {
                                AttributeOverrides attributeOverrides = entitySuperClass.getAnnotation(AttributeOverrides.class);
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
                                AttributeOverride attributeOverride = entitySuperClass.getAnnotation(AttributeOverride.class);
                                if (attributeOverride != null && a.getName().equals(attributeOverride.name())) {
                                    isUnique = attributeOverride.column().unique();
                                    isUniqueCalculated = true;
                                }
                            }
                            //TODO Test Column and JoinColumn Annotation for private Field
                            //Check Column Annotation
                            if (!isUniqueCalculated) {
                                Column column = null;
                                column = adapter.getAnnotation(Column.class);
                                if (column != null) {
                                    isUnique = column.unique();
                                    isUniqueCalculated = true;
                                }
                            }
                        } else {
                            //Check AssociationOverride Annotation
                            if (!isUniqueCalculated) {
                                AssociationOverrides associationOverrides = entitySuperClass.getAnnotation(AssociationOverrides.class);
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
                                AssociationOverride associationOverride = entitySuperClass.getAnnotation(AssociationOverride.class);
                                if (associationOverride != null && a.getName().equals(associationOverride.name())
                                        && associationOverride.joinColumns().length > 0) {
                                    isUnique = associationOverride.joinColumns()[0].unique();
                                    isUniqueCalculated = true;
                                }
                            }
                            //Check JoinColumn Annotation
                            if (!isUniqueCalculated) {
                                JoinColumn joinColumn = null;
                                joinColumn = adapter.getAnnotation(JoinColumn.class);
                                if (joinColumn != null) {
                                    isUnique = joinColumn.unique();
                                    isUniqueCalculated = true;
                                }
                            }
                        }
                        entitySuperClass = entitySuperClass.getSuperclass();
                    } while (!isUniqueCalculated
                            && (entitySuperClass.isAnnotationPresent(Entity.class)
                            || entitySuperClass.isAnnotationPresent(MappedSuperclass.class)));
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

        //Process UniqueConstraint annotation with hierarchy
        query = cb.createQuery(entityClass);
        root = query.from(entityType);
        Class<?> entitySuperClass = entityClass;
        while (entitySuperClass != null && entitySuperClass.isAnnotationPresent(Entity.class)) {
            if (entitySuperClass.isAnnotationPresent(Table.class)) {
                if (entitySuperClass.equals(entityClass)
                        || ((!entitySuperClass.isAnnotationPresent(Inheritance.class))
                        || entitySuperClass.getAnnotation(Inheritance.class).strategy() != InheritanceType.JOINED)) {
                    Object result = getByUniqueConstraintAnnotation(entityClass, entity, cpa);
                    if (result != null) {
                        return (T) result;
                    }
                } else {
                    Object result = getByUniqueConstraintAnnotation(entitySuperClass, entity, cpa);
                    if (result != null) {
                        if (entity.getClass().isAssignableFrom(result.getClass())) {
                            return (T) result;
                        } else {
                            //TODO Продумать сообщение
                            throw new RuntimeException("Entity with conflicted unique constraints exists and have other type");
                        }
                    }
                }
            }
            entitySuperClass = entitySuperClass.getSuperclass();
        }
        return null;
    }

    private Object getByUniqueConstraintAnnotation(Class<?> entityClass, Object entity, ClassPropertyAdapter cpa) {
        Table annotation = entityClass.getAnnotation(Table.class);
        if (annotation.uniqueConstraints() != null) {
            for (UniqueConstraint uniqueConstraint : annotation.uniqueConstraints()) {
                CriteriaQuery<?> query = cb.createQuery(entityClass);
                Root<?> root = query.from(em.getMetamodel().entity(entityClass));
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
                    List<?> list = em.createQuery(query).getResultList();
                    if (!list.isEmpty()) {
                        return list.get(0);
                    }
                }
                return null;
            }
        }
        return null;
    }
}
