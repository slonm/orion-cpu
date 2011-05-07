package ua.orion.core.services;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import org.tynamo.jpa.annotations.CommitAfter;
import ua.orion.persistence.MetaEntity;

/**
 * Основной сервис для работы с моделью данных.
 * Сущности модели могут быть дополнены двумя специальными атрибутами:
 * UserPresentable и UniqueKey подобно атрибутам Id и Version JPA.
 *
 * UserPresentable это уникальный transient атрибут, который строится на основе
 * доступных для просмотра пользователям атрибутах, например: название города в сущности "город"
 * (это простейший случай, когда UserPresentable совпадает с хранимым атрибутом),
 * номер, серия, дата выдачи в сущности "документ". Предназначен для однострочного
 * оформления упоминания экземпляра, например в User Friendly URL или в поле UI в
 * котором отображается ассоциация с UserPresentable сущностью.
 * Методы для работы с UserPresentable работают всегда. Если сущность
 * не поддерживает UserPresentable, то используется ее идентификатор.
 * Так как UserPresentable атрибут, в общем случае, не является хранимым, то его
 * значение нельзя задать. При этом, если атрибут оформлен как Formula Hibernate, то 
 * появляется возможность поиска по этому атрибуту
 *
 * UniqueKey это уникальный хранимый атрибут, который не может изменятся пользоваелем.
 * Используется для самодокументирующегося программного кода, например в запросах вместо
 * идентификатора экземпляра лучше использовать его уникальное символическое имя.
 * @author sl
 */
public interface EntityService {

    EntityManager getEntityManager();
    
    Set<Class<?>> getManagedEntities();
    
    MetaEntity getMetaEntity(Class<?> entityClass);

    /**
     * Возвращает MetaEntity по регистронезависимому имени сущности
     */
    MetaEntity getMetaEntity(String entityName);

    /**
     * Transactional wrapper for EntityManager.persist(object)
     */
    @CommitAfter
    void persist(Object entity);

    /**
     * newInstance of entity
     */
    <T> T newInstance(Class<T> entityClass);

    /**
     * Transactional wrapper for EntityManager.merge(object)
     */
    @CommitAfter
    <T> T merge(T entity);

    /**
     * Transactional wrapper for EntityManager.remove(object)
     */
    @CommitAfter
    void remove(Object entity);

    /**
     * Wrapper for EntityManager.find(entityClass, primaryKey)
     * primaryKey будет приведен к нужному типу с помощью TypeCoercer
     */
    <T> T find(Class<T> entityClass, Serializable primaryKey);

    /**
     * Wrapper for EntityManager.find(entityClass, primaryKey, lockMode)
     * primaryKey будет приведен к нужному типу с помощью TypeCoercer
     */
    public <T> T find(Class<T> entityClass, Serializable primaryKey,
            LockModeType lockMode);

    /**
     * Wrapper for EntityManager.getReference(entityClass, primaryKey)
     * primaryKey будет приведен к нужному типу с помощью TypeCoercer
     */
    public <T> T getReference(Class<T> entityClass,
            Serializable primaryKey);

    /*
     * Ищет экземпляр типа T в хранилище с которым конфликтует entity по ограничениям
     * уникальности и возвращает его. Если такого экземпляр нет сохраняет entity и
     * возвращает его
     */
    <T> T findUniqueOrPersist(T entity);

    /*
     * Находит экземпляр T по его Name атрибуту.
     * Если Name не уникален, то возвращает первый найденный объект
     * Если Name в сущности нет, то возникает есключение IllegalArgumentException
     * @todo архитектурно этому DAO методу тут не место
     */
    <T> T findByName(Class<T> type, String name);

    /*
     * Находит экземпляр T по его UserPresentable атрибуту
     */
    <T> T findByUserPresentable(Class<T> type, String name);

    /*
     * Возвращает значение UserPresentable атрибута у entity
     */
    String getUserPresentable(Object entity);

    /*
     * Находит объект по его UniqueKey атрибуту у entity
     * Если UKey не поддерживается или entity не сущность,
     * то возникает исключение IllegalArgumentException
     */
    <T> T findByUKey(Class<T> type, String uKey);

    /*
     * Возвращает значение UniqueKey атрибута у entity
     * Если UKey не поддерживается или entity не сущность,
     * то возникает исключение IllegalArgumentException
     */
    String getUKey(Object entity);

    /*
     * Устанавливает UniqueKey у entity
     * Если UKey не поддерживается или entity не сущность,
     * то возникает исключение IllegalArgumentException
     */
    void setUKey(Object entity, String uKey);

    /**
     * Устанавливает Id у entity
     * primaryKey будет приведен к нужному типу с помощью TypeCoercer
     */
    void setPrimaryKey(Object entity, Serializable primaryKey);

    /*
     * Возвращает значение Id атрибута у entity
     */
    Serializable getPrimaryKey(Object entity);
    
    /*
     * Возвращает значение Version атрибута у entity
     */
    Object getVersion(Object entity);
    
    /**
     * Discards all stored MetaEntity.
     */
    void clearCache();
}
