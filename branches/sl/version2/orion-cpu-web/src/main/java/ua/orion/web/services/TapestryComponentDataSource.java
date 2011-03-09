/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.web.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;

/**
 *
 * @author slobodyanuk
 */
public interface TapestryComponentDataSource {
    GridDataSource getGridDataSource(Class<?> entityClass);
    <T> BeanModel<T> getBeanModelForList(Class<T> clasz);
    <T> BeanModel<T> getBeanModelForView(Class<T> clasz);
    <T> BeanModel<T> getBeanModelForEdit(Class<T> clasz);
}
