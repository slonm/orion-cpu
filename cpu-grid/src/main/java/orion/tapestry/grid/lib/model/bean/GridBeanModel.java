package orion.tapestry.grid.lib.model.bean;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.RelativePosition;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.model.sort.GridSortModelImpl;
import orion.tapestry.grid.lib.model.view.GridPropertyViewModel;

/**
 *
 * @author dobro
 */
public interface GridBeanModel<T> extends BeanModel<T> {
//
//    @Override
//    public Class<T> getBeanType() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public T newInstance() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public List<String> getPropertyNames() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public GridPropertyModelInterface get(String propertyName);

    @Override
    public GridPropertyModelInterface getById(String propertyId);

    @Override
    public GridPropertyModelInterface add(String propertyName);

    @Override
    public GridPropertyModelInterface add(RelativePosition position, String existingPropertyName, String propertyName);

    @Override
    public GridPropertyModelInterface add(RelativePosition position, String existingPropertyName, String propertyName, PropertyConduit conduit);

    @Override
    public GridPropertyModelInterface add(String propertyName, PropertyConduit conduit);

    @Override
    public GridBeanModel<T> exclude(String... propertyNames);

    @Override
    public GridBeanModel<T> reorder(String... propertyNames);

    @Override
    public GridBeanModel<T> include(String... propertyNames);

    /**
     * Комбинирует существующие свойства модели
     * в объект GridPropertyViewModel
     * для управления видимостью.
     */
    public GridPropertyViewModel getGridPropertyViewModel();

    /**
     * Комбинирует существующие свойства модели
     * в объект GridSortModelImpl
     * для управления сортировкой.
     */
    public GridSortModelImpl getGridSortModelImpl();

    /**
     * Комбинирует существующие свойства модели
     * в объект GridFilterModel
     * для управления фильтрацией.
     */
    public GridFilterModel getGridFilterModel();

    /**
     * Устанавливает источник сообщений для интерфейса
     */
    public void setMessage(Messages msg);
}
