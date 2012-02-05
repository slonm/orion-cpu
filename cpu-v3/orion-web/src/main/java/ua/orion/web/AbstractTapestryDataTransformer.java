/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.web.services.TapestryDataTransformer;

/**
 *
 * @author sl
 */
public abstract class AbstractTapestryDataTransformer implements TapestryDataTransformer{

    @Override
    public GridDataSource transformGridDataSource(GridDataSource ds) {
        return ds;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages) {
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForView(BeanModel<T> model, Messages messages) {
        return model;
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        return model;
    }

    @Override
    public <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property) {
        return model;
    }

    @Override
    public <T> SelectModel transformSelectModel(SelectModel model, T entity, String property) {
        return model;
    }

    @Override
    public String transformCrudPage(String page, Class<?> entityClass) {
        return page;
    }
}
