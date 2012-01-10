package ua.orion.web.services;

import java.lang.reflect.Modifier;
import java.util.*;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.orion.core.services.*;

/**
 *
 * @author slobodyanuk
 */
public class TapestryDataFactoryImpl implements TapestryDataFactory {

    private final EntityService es;
    private final BeanModelSource beanModelSource;
    private final ApplicationMessagesSource messagesSource;
    private final ModelLabelSource modelLabelSource;
    private final PropertyAccess propertyAccess;

    public TapestryDataFactoryImpl(EntityService entityService,
            BeanModelSource beanModelSource,
            ApplicationMessagesSource messagesSource,
            ModelLabelSource modelLabelSource,
            PropertyAccess propertyAccess) {
        this.es = entityService;
        this.beanModelSource = beanModelSource;
        this.messagesSource = messagesSource;
        this.modelLabelSource = modelLabelSource;
        this.propertyAccess = propertyAccess;
    }

    @Override
    public GridDataSource createGridDataSource(Class<?> entityClass) {
        return new JpaGridDataSource(es.getEntityManager(), entityClass);
    }

    @Override
    public <T> BeanModel<T> createBeanModelForList(Class<T> clasz) {
        return createBeanModelForList(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> createBeanModelForList(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createDisplayModel(clasz, messages);
        removeStaticFields(bm);
        setCellLabels(bm, messages);
        //TODO Hide some user defined fields
        return bm;
    }

    @Override
    public <T> BeanModel<T> createBeanModelForView(Class<T> clasz) {
        return createBeanModelForView(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> createBeanModelForView(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createDisplayModel(clasz, messages);
        removeStaticFields(bm);
        setLabels(bm, messages);
        return bm;
    }

    @Override
    public <T> BeanModel<T> createBeanModelForEdit(Class<T> clasz) {
        return createBeanModelForEdit(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> createBeanModelForEdit(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createEditModel(clasz, messages);
        removeStaticFields(bm);
        setLabels(bm, messages);
        return bm;
    }

    @Override
    public <T> BeanModel<T> createBeanModelForAdd(Class<T> clasz) {
        return createBeanModelForEdit(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> createBeanModelForAdd(Class<T> clasz, Messages messages) {
        return createBeanModelForEdit(clasz, messages);
    }

    private void removeStaticFields(BeanModel<?> model) {
        ClassPropertyAdapter adapter = propertyAccess.getAdapter(model.getBeanType());
        List<String> propertyNames = new ArrayList<String>();
        for (String name : model.getPropertyNames()) {
            PropertyAdapter pa = adapter.getPropertyAdapter(name);
            if (isStaticFieldProperty(pa)) {
                propertyNames.add(name);
            }
        }
        if (!propertyNames.isEmpty()) {
            model.exclude(propertyNames.toArray(new String[propertyNames.size()]));
        }
    }

    private boolean isStaticFieldProperty(PropertyAdapter adapter) {
        return adapter.isField() && Modifier.isStatic(adapter.getField().getModifiers());
    }

    private void setLabels(BeanModel<?> model, Messages messages) {
        for (String name : model.getPropertyNames()) {
            String label = modelLabelSource.getPropertyLabel(model.getBeanType(), name, messages);
            if (label != null) {
                model.get(name).label(label);
            }
        }
    }

    private void setCellLabels(BeanModel<?> model, Messages messages) {
        for (String name : model.getPropertyNames()) {
            String label = modelLabelSource.getCellPropertyLabel(model.getBeanType(), name, messages);
            if (label != null) {
                model.get(name).label(label);
            }
        }
    }

    @Override
    public <T> SelectModel createSelectModel(Class<T> entityClass, String property) {
        CriteriaQuery<T> query = es.createQuery(propertyAccess.getAdapter(entityClass).getPropertyAdapter(property).getType());
        List<T> objects = es.getEntityManager().createQuery(query).getResultList();
        final List<OptionModel> options = new ArrayList<OptionModel>();
        for (final T object : objects) {
            options.add(new AbstractOptionModel() {

                @Override
                public String getLabel() {
                    return es.getStringValue(object);
                }

                @Override
                public Object getValue() {
                    return object;
                }
            });
        }
        return new AbstractSelectModel() {

            @Override
            public List<OptionGroupModel> getOptionGroups() {
                return null;
            }

            @Override
            public List<OptionModel> getOptions() {
                return options;
            }
        };
    }

    @Override
    public <T> SelectModel createSelectModel(T entity, String property) {
        return createSelectModel(entity.getClass(), property);
    }

    @Override
    public String createCrudPage(Class<?> entityClass) {
        return "ori/crud";
    }
}
