/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.grid.services;

import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;

/**
 *
 * @author dobro
 */
public interface GridBeanModelSource {
    /**
     * Creates a model for display purposes; this may include properties which are read-only.
     *
     * @param beanClass class of object to be edited
     * @param messages
     * @return a model containing properties that can be presented to the user
     */
    <T> GridBeanModel<T> createDisplayModel(Class<T> beanClass, Messages messages);

}
