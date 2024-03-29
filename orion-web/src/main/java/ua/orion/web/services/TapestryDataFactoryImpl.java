package ua.orion.web.services;

import java.lang.reflect.Modifier;
import java.util.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.orion.core.services.*;
import ua.orion.web.AdditionalConstraintsApplier;
import ua.orion.web.FetchAllJpaGridDataSource;

/**
 *
 * @author slobodyanuk
 */
public class TapestryDataFactoryImpl implements TapestryDataFactory {

    private final EntityService es;
    private final BeanModelSource beanModelSource;
    private final ModelLabelSource modelLabelSource;
    private final PropertyAccess propertyAccess;

    public TapestryDataFactoryImpl(EntityService entityService,
            BeanModelSource beanModelSource,
            ModelLabelSource modelLabelSource,
            PropertyAccess propertyAccess) {
        this.es = entityService;
        this.beanModelSource = beanModelSource;
        this.modelLabelSource = modelLabelSource;
        this.propertyAccess = propertyAccess;
    }

    @Override
    public GridDataSource createGridDataSource(Class<?> entityClass) {
        return new FetchAllJpaGridDataSource(es.getEntityManager(), entityClass, propertyAccess);
    }

    @Override
    public <E> GridDataSource createGridDataSource(Class<E> entityClass, final AdditionalConstraintsApplier<E> applier) {
        return new FetchAllJpaGridDataSource(es.getEntityManager(), entityClass, propertyAccess) {

            @Override
            protected void applyAdditionalConstraints(CriteriaQuery criteria, Root root, CriteriaBuilder builder) {
                applier.applyAdditionalConstraints(criteria, root, builder);
            }
        };
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
    public <T> BeanModel<T> createBeanModelForView(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createDisplayModel(clasz, messages);
        removeStaticFields(bm);
        setLabels(bm, messages);
        return bm;
    }

    @Override
    public <T> BeanModel<T> createBeanModelForEdit(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createEditModel(clasz, messages);
        removeStaticFields(bm);
        setLabels(bm, messages);
        return bm;
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
    public SelectModel createSelectModel(Class<?> entityClass, String property) {
        Class<?> cls=propertyAccess.getAdapter(entityClass).getPropertyAdapter(property).getType();
        CriteriaQuery<?> query = es.getEntityManager().getCriteriaBuilder().createQuery(cls);
        query.from(cls);
        List<?> objects = es.getEntityManager().createQuery(query).getResultList();
        final List<OptionModel> options = new ArrayList<OptionModel>();
        for (final Object object : objects) {
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
    public SelectModel createSelectModel(Object entity, String property) {
        return createSelectModel(entity.getClass(), property);
    }

    @Override
    public String createCrudPage(Class<?> entityClass) {
        return "ori/crud";
    }
}
