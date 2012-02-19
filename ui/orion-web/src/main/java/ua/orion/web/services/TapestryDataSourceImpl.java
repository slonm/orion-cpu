package ua.orion.web.services;

import java.util.List;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import ua.orion.web.AdditionalConstraintsApplier;

/**
 *
 * @author sl
 */
public class TapestryDataSourceImpl implements TapestryDataSource {

    private final TapestryDataFactory factory;
    private final List<TapestryDataTransformer> transformers;
    private final Messages messages;

    public TapestryDataSourceImpl(TapestryDataFactory factory, List<TapestryDataTransformer> transformers, Messages messages) {
        this.factory = factory;
        this.transformers = transformers;
        this.messages = messages;
    }

    @Override
    public GridDataSource getGridDataSource(Class<?> entityClass) {
        GridDataSource ret = factory.createGridDataSource(entityClass);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformGridDataSource(ret);
        }
        return ret;
    }

    @Override
    public <E> GridDataSource getGridDataSource(Class<E> entityClass, AdditionalConstraintsApplier<E> applier) {
        return factory.createGridDataSource(entityClass, applier);
    }
    
    @Override
    public <T> BeanModel<T> getBeanModelForList(Class<T> entityClass) {
        return getBeanModelForList(entityClass, messages);
    }

    @Override
    public <T> BeanModel<T> getBeanModelForList(Class<T> entityClass, Messages messages) {
        BeanModel<T> ret = factory.createBeanModelForList(entityClass, messages);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformBeanModelForList(ret, messages);
        }
        return ret;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForAdd(Class<T> entityClass) {
        return getBeanModelForAdd(entityClass, messages);
    }

    @Override
    public <T> BeanModel<T> getBeanModelForAdd(Class<T> entityClass, Messages messages) {
        BeanModel<T> ret = factory.createBeanModelForAdd(entityClass, messages);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformBeanModelForAdd(ret, messages);
        }
        return ret;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForView(Class<T> entityClass) {
        return getBeanModelForView(entityClass, messages);
    }

    @Override
    public <T> BeanModel<T> getBeanModelForView(Class<T> entityClass, Messages messages) {
        BeanModel<T> ret = factory.createBeanModelForView(entityClass, messages);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformBeanModelForView(ret, messages);
        }
        return ret;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForEdit(Class<T> entityClass) {
        return getBeanModelForEdit(entityClass, messages);
    }

    @Override
    public <T> BeanModel<T> getBeanModelForEdit(Class<T> entityClass, Messages messages) {
        BeanModel<T> ret = factory.createBeanModelForEdit(entityClass, messages);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformBeanModelForEdit(ret, messages);
        }
        return ret;
    }

    @Override
    public <T> SelectModel getSelectModel(Class<T> entityClass, String property) {
        SelectModel ret = factory.createSelectModel(entityClass, property);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformSelectModel(ret, entityClass, property);
        }
        return ret;
    }

    @Override
    public <T> SelectModel getSelectModel(T entity, String property) {
        SelectModel ret = factory.createSelectModel(entity, property);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformSelectModel(ret, entity, property);
        }
        return ret;
    }

    @Override
    public String getCrudPage(Class<?> entityClass) {
        String ret = factory.createCrudPage(entityClass);
        for (TapestryDataTransformer trans : transformers) {
            ret = trans.transformCrudPage(ret, entityClass);
        }
        return ret;
    }

}
