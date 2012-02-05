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
public interface TapestryDataTransformer {
    GridDataSource transformGridDataSource(GridDataSource ds);
    <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages);
    <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages);
    <T> BeanModel<T> transformBeanModelForView(BeanModel<T> model, Messages messages);
    <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages);
    <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property);
    <T> SelectModel transformSelectModel(SelectModel model, T entity, String property);
    String transformCrudPage(String page, Class<?> entityClass);
}
