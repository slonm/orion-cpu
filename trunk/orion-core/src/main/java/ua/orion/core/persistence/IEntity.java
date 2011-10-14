/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.persistence;

import java.sql.Timestamp;

/**
 * Интерфейс с методами абстрактной сущности {@link ua.orion.core.persistence.AbstractEntity}
 * @author slobodyanuk
 */
public interface IEntity {

    /**
     * @return идентификатор объекта сущности
     */
    Integer getId();

    void setId(Integer id);
    
    /**
     * @return временную метку последнего изменения объекта сущности
     */
    Timestamp getLastChange();

    void setLastChange(Timestamp lastChange);
}
