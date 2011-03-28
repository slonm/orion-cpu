package orion.tapestry.grid.lib.field.impl;

import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * Колонка, которая отображается в таблице,
 * но не имеет прямого аналога в базе данных
 * @author Gennadiy Dobrovolsky
 */
public class GridFieldCalculable extends GridFieldAbstract<Object> {

    /**
     * @param _uid  уникальный идентификатор поля
     */
    public GridFieldCalculable(String _uid) {
        this.setFilterElementList(null);
        this.setFieldSort(null);

        GridFieldView extraFieldView = new GridFieldView();
        extraFieldView._setIsVisible(true)._setOrdering(0)._setUid(_uid)._setLabel(_uid+"-label");
        this.setFieldView(extraFieldView);

        this.setAttributeName(null);
        this.setUid(_uid);
        this.setLabel(_uid + "-label");
    }

    public GridFieldCalculable() {
    }

    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        return null;
    }

    @Override
    public String getStringValue(GridRow row) {
        return "";
    }

    public Object getValue(GridRow row) {
        return null;
    }
}
