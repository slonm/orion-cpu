/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.core.persistence;

import java.sql.Timestamp;

/**
 *
 * @author slobodyanuk
 */
public interface IEntity {
    Timestamp getLastChange();

    void setLastChange(Timestamp lastChange);

    Integer getId();

    void setId(Integer id);
}
