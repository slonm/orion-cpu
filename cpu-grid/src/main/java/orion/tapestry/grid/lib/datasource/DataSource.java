package orion.tapestry.grid.lib.datasource;

import java.util.List;
import org.apache.tapestry5.grid.SortConstraint;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;

/**
 *
 * @author dobro
 */
public interface DataSource extends org.apache.tapestry5.grid.GridDataSource {
//    /**
//     * Returns the number of rows available in the data source.
//     */
//    int getAvailableRows();
//
//    /**
//     * Invoked to allow the source to prepare to present values. This gives the source a chance to pre-fetch data (when
//     * appropriate) and informs the source of the desired sort order.  Sorting comes first, then extraction by range.
//     *
//     * @param startIndex      the starting index to be retrieved
//     * @param endIndex        the ending index to be retrieved
//     * @param sortConstraints identify how data is to be sorted
//     */
//    void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints);
//
//    /**
//     * Returns the row value at the provided index. This method will be invoked in sequential order. In rare instances,
//     * {@link #getAvailableRows()} may return a different number of rows than are actually available (i.e., the database
//     * was changed between calls to {@link #getAvailableRows()} and the call to {@link #prepare(int, int,
//     * java.util.List)}).  In that case, this method should return null for any out-of-range indexes.
//     */
//    Object getRowValue(int index);
//
//    /**
//     * Returns the type of value in the rows, or null if not known. This value is used to create a default {@link
//     * org.apache.tapestry5.beaneditor.BeanModel} when no such model is explicitly provided.
//     *
//     * @return the row type, or null
//     */
//    Class getRowType();

    /**
     * Invoked to allow the source to prepare to present values.
     * This gives the source a chance to pre-fetch data (when
     * appropriate) and informs the source of the desired sort order.
     * Sorting comes first, then extraction by range.
     *
     * @param startIndex      the starting index to be retrieved
     * @param endIndex        the ending index to be retrieved
     * @param sortConstraints identify how data is to be sorted
     * @param filterJSON      JSON formatted string representing row filter
     */
    void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints, String filterJSON);
}
