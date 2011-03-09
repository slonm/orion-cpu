package ua.orion.web.services;

import java.util.Locale;
import javax.persistence.metamodel.Metamodel;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.LocalizationSetter;
import ua.orion.core.services.ApplicationMessagesSource;
import ua.orion.core.services.EntityService;

/**
 *
 * @author slobodyanuk
 */
public class TapestryComponentDataSourceImpl implements TapestryComponentDataSource{

//    private final EntityService entityService;
    private final BeanModelSource beanModelSource;
    private final ApplicationMessagesSource messagesSource;
//    private final Metamodel metamodel;

    @Override
    public GridDataSource getGridDataSource(Class<?> entityClass) {
        throw new UnsupportedOperationException("Not supported yet.");
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
