package orion.tapestry.grid.lib.datasource;

import java.util.List;
import org.apache.tapestry5.grid.SortConstraint;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;

/**
 *
 * @author dobro
 */
public abstract class DataSourceAdapter implements DataSource {

    @Override
    public abstract Object getRowValue(int index);

    @Override
    public abstract Class getRowType();

    @Override
    public abstract void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints, String filterJSON);

    @Override
    public abstract int getAvailableRows();
    // адаптер для старого метода, который не умеют фильтровать строки

    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        prepare(startIndex, endIndex, sortConstraints, null);
    }
}
