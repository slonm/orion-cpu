package orion.tapestry.grid.components;

import java.util.List;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 *
 * @author dobro
 */
public class GridColumns {
    /**
     * Список колонок
     */
    @Parameter(principal = true)
    @SuppressWarnings("unused")
    @Property
    private List<GridPropertyModelInterface> columnList;
    /**
     * Временная переменная для цикла по колонкам
     * Цикл объявлен в шаблоне
     */
    @Parameter(principal = true)
    @SuppressWarnings("unused")
    @Property
    private GridPropertyModelInterface columnModel;

    @Inject
    private Block standardHeader;

    /**
     * Optional output parameter that stores the current column index.
     */
    @Parameter
    @Property
    private int index;

    /**
     * Defines where block and label overrides are obtained from.
     * By default, the Grid component provides block
     * overrides (from its block parameters).
     */
    @Parameter(value = "this", allowNull = false)
    @Property(write = false)
    private PropertyOverrides overrides;

    public Block getBlockForColumn()
    {
        Block override = overrides.getOverrideBlock(columnModel.getId() + "ColumnHeader");

        if (override != null) return override;

        return standardHeader;
    }
}
