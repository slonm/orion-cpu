package ua.orion.web.services;

import java.util.Locale;
import javax.persistence.metamodel.Metamodel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.services.BeanModelSource;
import org.tynamo.jpa.internal.JPAGridDataSource;
import ua.orion.core.services.ApplicationMessagesSource;
import ua.orion.core.services.EntityService;

/**
 *
 * @author slobodyanuk
 */
public class TapestryComponentDataSourceImpl implements TapestryComponentDataSource{

    private final EntityService entityService;
    private final BeanModelSource beanModelSource;
    private final ApplicationMessagesSource messagesSource;

    public TapestryComponentDataSourceImpl(EntityService entityService, BeanModelSource beanModelSource, ApplicationMessagesSource messagesSource) {
        this.entityService = entityService;
        this.beanModelSource = beanModelSource;
        this.messagesSource = messagesSource;
    }
//    private final Metamodel metamodel;

    @Override
    public GridDataSource getGridDataSource(Class<?> entityClass) {
        return new JPAGridDataSource(entityService.getEntityManager(), entityClass);
    }

    @Override
    public <T> BeanModel<T> getBeanModelForList(Class<T> clasz) {
        BeanModel<T> bm=beanModelSource.createDisplayModel(clasz, messagesSource.getMessages());
        //TODO Hide some user defined fields
        return bm;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForView(Class<T> clasz) {
        BeanModel<T> bm=beanModelSource.createDisplayModel(clasz, messagesSource.getMessages());
        return bm;
    }

    @Override
    public <T> BeanModel<T> getBeanModelForEdit(Class<T> clasz) {
        BeanModel<T> bm=beanModelSource.createEditModel(clasz, messagesSource.getMessages());
        return bm;
    }
}
