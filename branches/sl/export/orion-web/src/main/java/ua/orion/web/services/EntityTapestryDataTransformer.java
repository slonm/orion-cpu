package ua.orion.web.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.services.ComponentClassResolver;
import ua.orion.core.services.EntityService;

/**
 *
 * @author sl
 */
public class EntityTapestryDataTransformer implements TapestryDataTransformer {

    private final Map<Class, TapestryDataTransformer> map = new HashMap();

    public EntityTapestryDataTransformer(ComponentClassResolver resolver,
            EntityService es,
            ClassNameLocator classNameLocator,
            ObjectLocator locator) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String packageName : resolver.getFolderToPackageMapping().values()) {
            Collection<String> classes = classNameLocator.locateClassNames(packageName);
            for (Class clasz : es.getManagedEntities()) {
                String className = packageName + ".transformers." + clasz.getSimpleName() + "TapestryDataTransformer";
                if (classes.contains(className)) {
                    try {
                        Class<TapestryDataTransformer> entityClass = (Class<TapestryDataTransformer>) contextClassLoader.loadClass(className);
                        map.put(clasz, locator.autobuild(entityClass));
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        }
    }

    @Override
    public GridDataSource transformGridDataSource(GridDataSource ds) {
        if (map.containsKey(ds.getRowType())) {
            return map.get(ds.getRowType()).transformGridDataSource(ds);
        } else {
            return ds;
        }
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForList(BeanModel<T> model, Messages messages) {
        if (map.containsKey(model.getBeanType())) {
            return map.get(model.getBeanType()).transformBeanModelForList(model, messages);
        } else {
            return model;
        }
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        if (map.containsKey(model.getBeanType())) {
            return map.get(model.getBeanType()).transformBeanModelForAdd(model, messages);
        } else {
            return model;
        }
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForView(BeanModel<T> model, Messages messages) {
        if (map.containsKey(model.getBeanType())) {
            return map.get(model.getBeanType()).transformBeanModelForView(model, messages);
        } else {
            return model;
        }
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        if (map.containsKey(model.getBeanType())) {
            return map.get(model.getBeanType()).transformBeanModelForEdit(model, messages);
        } else {
            return model;
        }
    }

    @Override
    public SelectModel transformSelectModel(SelectModel model, Class<?> entityClass, String property) {
        if (map.containsKey(entityClass)) {
            return map.get(entityClass).transformSelectModel(model, entityClass, property);
        } else {
            return model;
        }
    }

    @Override
    public SelectModel transformSelectModel(SelectModel model, Object entity, String property) {
        if (map.containsKey(entity.getClass())) {
            return map.get(entity.getClass()).transformSelectModel(model, entity, property);
        } else {
            return model;
        }
    }

    @Override
    public String transformCrudPage(String page, Class<?> entityClass) {
        if (map.containsKey(entityClass)) {
            return map.get(entityClass).transformCrudPage(page, entityClass);
        } else {
            return page;
        }
    }
}
