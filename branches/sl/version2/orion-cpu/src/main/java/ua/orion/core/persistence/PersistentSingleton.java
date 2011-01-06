/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.persistence;

import java.io.Serializable;
import ua.orion.persistence.annotations.UniqueKey;

/**
 *
 * @author sl
 */
@UniqueKey("name")
public interface PersistentSingleton<T extends Serializable> {

    String getName();

    void setName(String name);

    T getSingleton();

    void setSingleton(T singleton);
    
    Class<T> getSingletonClass();
}
