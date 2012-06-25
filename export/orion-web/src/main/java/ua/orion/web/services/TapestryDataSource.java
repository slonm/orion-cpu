package ua.orion.web.services;

import ua.orion.web.AdditionalConstraintsApplier;
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
    <E> GridDataSource getGridDataSource(Class<E> entityClass, AdditionalConstraintsApplier<E> applier);
    <T> BeanModel<T> getBeanModelForList(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForList(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> getBeanModelForAdd(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForAdd(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> getBeanModelForView(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForView(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> getBeanModelForEdit(Class<T> entityClass);
    <T> BeanModel<T> getBeanModelForEdit(Class<T> entityClass, Messages messages);
    SelectModel getSelectModel(Class<?> entityClass, String property);
    SelectModel getSelectModel(Object entity, String property);
    String getCrudPage(Class<?> entityClass);
}
