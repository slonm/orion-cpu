package ua.orion.web.services;

import java.util.*;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.util.AbstractSelectModel;
import org.tynamo.jpa.internal.JPAGridDataSource;
import ua.orion.core.services.*;
import ua.orion.core.utils.Defense;

/**
 *
 * @author slobodyanuk
 */
public class TapestryComponentDataSourceImpl implements TapestryComponentDataSource {

    private final EntityService es;
    private final BeanModelSource beanModelSource;
    private final ApplicationMessagesSource messagesSource;
    private final ModelLabelSource modelLabelSource;

    public TapestryComponentDataSourceImpl(EntityService entityService, BeanModelSource beanModelSource, ApplicationMessagesSource messagesSource, ModelLabelSource modelLabelSource) {
        this.es = entityService;
        this.beanModelSource = beanModelSource;
        this.messagesSource = messagesSource;
        this.modelLabelSource = modelLabelSource;
    }

//    private final Metamodel metamodel;
    @Override
    public GridDataSource getGridDataSource(Class<?> entityClass) {
        return new JPAGridDataSource(es.getEntityManager(), entityClass);
    }

    @Override
    public <T> BeanModel<T> getBeanModelForList(Class<T> clasz) {
        return getBeanModelForList(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> getBeanModelForList(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createDisplayModel(clasz, messages);
        setCellLabels(bm, messages);
        //TODO Hide some user defined fields
        return bm;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForView(Class<T> clasz) {
        return getBeanModelForView(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> getBeanModelForView(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createDisplayModel(clasz, messages);
        setLabels(bm, messages);
        return bm;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForEdit(Class<T> clasz) {
        return getBeanModelForEdit(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> getBeanModelForEdit(Class<T> clasz, Messages messages) {
        BeanModel<T> bm = beanModelSource.createEditModel(clasz, messages);
        setLabels(bm, messages);
        return bm;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForAdd(Class<T> clasz) {
        return getBeanModelForEdit(clasz, messagesSource.getMessages());
    }

    @Override
    public <T> BeanModel<T> getBeanModelForAdd(Class<T> clasz, Messages messages) {
        return getBeanModelForEdit(clasz, messages);
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
    public <T> SelectModel getSelectModel(Class<T> entityClass, String property) {
        Defense.notNull(entityClass, "entityClass");
        Defense.notBlank(property, "property");
        CriteriaQuery<T> query = es.createQuery(entityClass);
        List<T> objects = es.getEntityManager().createQuery(query).getResultList();
        final List<OptionModel> options = new ArrayList<OptionModel>();
        for (final T object : objects) {
            options.add(new AbstractOptionModel() {

                @Override
                public String getLabel() {
                    return String.valueOf(object);
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
    public <T> SelectModel getSelectModel(T entity, String property) {
        Defense.notNull(entity, "entity");
        Defense.notBlank(property, "property");
        return getSelectModel(entity.getClass(), property);
    }

    @Override
    public String getCrudPage(Class<?> entityClass) {
        return "ori/crud";
    }
}
