package orion.tapestry.grid.lib.datasource.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.datasource.DataSourceAdapter;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;

/**
 *
 * @author dobro
 */
@SuppressWarnings("all")
public class ListDataSource extends DataSourceAdapter {

    private Logger logger = LoggerFactory.getLogger(ListDataSource.class);
    private final List list;

    public ListDataSource(final List _list) {
        assert _list != null;
        list = _list;
        logger.info("Constucting ListGridDataSource() from " + _list);
    }

    @Override
    public Object getRowValue(int index) {
        return list.get(index);
    }

    @Override
    public Class getRowType() {
        return list.isEmpty() ? null : list.get(0).getClass();
    }

    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints, String filterJSON) {
        for (SortConstraint constraint : sortConstraints) {
            if (constraint == null) {
                continue;
            }

            final ColumnSort sort = constraint.getColumnSort();

            if (sort == ColumnSort.UNSORTED) {
                continue;
            }

            final PropertyConduit conduit = constraint.getPropertyModel().getConduit();

            final Comparator valueComparator = new Comparator<Comparable>() {

                @Override
                public int compare(Comparable o1, Comparable o2) {
                    // Simplify comparison, and handle case where both are nulls.

                    if (o1 == o2) {
                        return 0;
                    }

                    if (o2 == null) {
                        return 1;
                    }

                    if (o1 == null) {
                        return -1;
                    }

                    return o1.compareTo(o2);
                }
            };

            final Comparator rowComparator = new Comparator() {

                public int compare(Object row1, Object row2) {
                    Comparable value1 = (Comparable) conduit.get(row1);
                    Comparable value2 = (Comparable) conduit.get(row2);

                    return valueComparator.compare(value1, value2);
                }
            };

            final Comparator reverseComparator = new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    int modifier = sort == ColumnSort.ASCENDING ? 1 : -1;

                    return modifier * rowComparator.compare(o1, o2);
                }
            };

            // We can freely sort this list because its just a copy.

            Collections.sort(list, reverseComparator);
        }
    }

    @Override
    public int getAvailableRows() {
        return list.size();
    }
}
