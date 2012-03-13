package ua.orion.web.services;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.web.AdditionalConstraintsApplier;

/**
 * Источник моделей данных и источников данных на основе сущностей JPA для компонентов
 * Tapestry
 * @author slobodyanuk
 */
public interface TapestryDataFactory {
    GridDataSource createGridDataSource(Class<?> entityClass);
    <E> GridDataSource createGridDataSource(Class<E> entityClass, AdditionalConstraintsApplier<E> applier);
    <T> BeanModel<T> createBeanModelForList(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> createBeanModelForAdd(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> createBeanModelForView(Class<T> entityClass, Messages messages);
    <T> BeanModel<T> createBeanModelForEdit(Class<T> entityClass, Messages messages);
    SelectModel createSelectModel(Class<?> entityClass, String property);
    SelectModel createSelectModel(Object entity, String property);
    String createCrudPage(Class<?> entityClass);
}
