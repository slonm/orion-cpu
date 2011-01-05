package ua.orion.core.services;

import java.io.Serializable;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import org.apache.tapestry5.ioc.Messages;
import org.tynamo.jpa.annotations.CommitAfter;
import ua.orion.core.persistence.MetaEntity;

/**
 * методы UserPresentable работают всегда. Если сущность не поддерживает UserPresentable,
 * то используется ее идентификатор.
 * @author sl
 */
public interface EntityService {

    EntityManager getEntityManager();
    
    MetaEntity getMetaEntity(Class<?> entityClass);

    @CommitAfter
    void persist(Object entity);

    @CommitAfter
    <T> T merge(T entity);

    @CommitAfter
    void remove(Object entity);

    /**
     * primaryKey будет приведен к нужному типу с помощью TypeCoercer
     * @param <T>
     * @param entityClass
     * @param primaryKey
     * @return 
     */
    <T> T find(Class<T> entityClass, Serializable primaryKey);

    public <T> T find(Class<T> entityClass, Serializable primaryKey,
            LockModeType lockMode);

    public <T> T getReference(Class<T> entityClass,
            Serializable primaryKey);

    /*
     * Сохраняет объект и возвращает его, если выполняются условия уникальности,
     * в противном случае возвращает найденый уникальный персистентный объект
     */
    <T> T persistOrGet(T entity);

    /*
     * Находит объект по его Name атрибуту.
     * Если Name не уникален, то возвращает первый найденный объект
     * Если Name в сущности нет, то возникает есключение IllegalArgumentException
     */
    <T> T findByName(Class<T> type, String name);

    /*
     * Находит объект по его UserPresentable атрибуту
     */
    <T> T findByUserPresentableOrPrimaryKey(Class<T> type, String name);

    /*
     * Возвращает значение UserPresentable атрибута
     */
    String getUserPresentableOrPrimaryKey(Object entity);

    /*
     * Находит объект по его UniqueKey атрибуту
     * Если UKey не поддерживается или entity не сущность, то возникает исключение IllegalArgumentException
     */
    <T> T findByUKey(Class<T> type, String uKey);

    /*
     * Возвращает значение UniqueKey атрибута
     * Если UKey не поддерживается или entity не сущность, то возникает исключение IllegalArgumentException
     */
    String getUKey(Object entity);

    /*
     * Устанавливает UniqueKey у сущности
     * Если UKey не поддерживается или entity не сущность, то возникает исключение IllegalArgumentException
     */
    void setUKey(Object entity, String uKey);

    /**
     * primaryKey будет приведен к нужному типу с помощью TypeCoercer
     */
    void setPrimaryKey(Object entity, Serializable primaryKey);

    Serializable getPrimaryKey(Object entity);
    
    Object getVersion(Object entity);
}
