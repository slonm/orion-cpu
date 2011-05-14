package ua.orion.web.services;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;

/**
 * Источник моделей данных и источников данных на основе сущностей JPA для компонентов
 * Tapestry
 * @author slobodyanuk
 */
public interface TapestryDataSource {
    GridDataSource getGridDataSource(Class<?> entityClass);
    <T> BeanModel<T> getBeanModelForList(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForList(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> getBeanModelForAdd(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForAdd(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> getBeanModelForView(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForView(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> getBeanModelForEdit(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForEdit(Class<T> entityClass, Messages messages);
    <T> SelectModel getSelectModel(Class<T> entityClass, String property);
    <T> SelectModel getSelectModel(T entity, String property);
    String getCrudPage(Class<?> entityClass);
}
