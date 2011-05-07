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
public interface PersistentSingleton<T extends Serializable> extends INamed {

    @UniqueKey()
    @Override
    String getName();

    T getSingleton();

    void setSingleton(T singleton);

    Class<T> getSingletonClass();
}
