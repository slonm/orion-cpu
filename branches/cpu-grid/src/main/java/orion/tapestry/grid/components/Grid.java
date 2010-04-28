package orion.tapestry.grid.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;

/**
 * Компонента для представления табличных данных
 * @author Gennadiy Dobrovolsky
 */
@IncludeStylesheet({"Grid.css"})
@SupportsInformalParameters
public class Grid {

    /**
     * A comma-separated list of property names indicating the order in which the properties should be presented. The
     * names are case insensitive. Any properties not indicated in the list will be appended to the end of the display
     * order.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String reorder;
    /**
     * Used to store the current object being rendered (for the current row). This is used when parameter blocks are
     * provided to override the default cell renderer for a particular column ... the components within the block can
     * use the property bound to the row parameter to know what they should render.
     */
    @Parameter(principal = true)
    private Object row;

    /**
     * Optional output parameter used to identify the index of the column being rendered.
     */
    @Parameter
    private int columnIndex;

    /**
     * The number of rows of data displayed on each page. If there are more rows than will fit, the Grid will divide up
     * the rows into "pages" and (normally) provide a pager to allow the user to navigate within the overall result
     * set.
     */
    @Parameter("25")
    private int rowsPerPage;
    /**
     * A Block to render instead of the table (and pager, etc.) when the source is empty. The default is simply the text
     * "There is no data to display". This parameter is used to customize that message, possibly including components to
     * allow the user to create new objects.
     */
    @Parameter(value = "block:empty", defaultPrefix = BindingConstants.LITERAL)
    private Block empty;
    /**
     * CSS class for the &lt;table&gt; element.  In addition, informal parameters to the Grid are rendered in the table
     * element.
     */
    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL, value = "t-data-grid")
    @Property(write = false)
    private String tableClass;
}
