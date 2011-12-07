package orion.tapestry.grid.lib.model.sort;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * Сортировка по одной колонке
 * но учитывается возможность сортировки по нескольким колонкам одновременно
 * для чего добавлена возможность упорядочения правил сортировки
 * @author dobro
 */
public class GridSortConstraint extends SortConstraint {

    /**
     * свойство, к которому относится это условие сортировки
     */
    private final GridPropertyModelInterface propertyModel;
    /**
     * Способ сортировки (по возрастанию, по убыванию, не сортировать)
     */
    private ColumnSort columnSort;
    /**
     * Приоритет при сортировке по нескольким свойствам одновременно
     */
    private int priority = 0;

    public GridSortConstraint(GridPropertyModelInterface _propertyModel, ColumnSort _columnSort, int _priority) {
        super(_propertyModel, _columnSort);

        assert _propertyModel != null;
        assert _columnSort != null;

        this.propertyModel = _propertyModel;
        this.columnSort = _columnSort;
    }

    /**
     * Возвращает модель свойства, для которого составлено это правило сортировки
     */
    @Override
    public GridPropertyModelInterface getPropertyModel() {
        return propertyModel;
    }

    /**
     * Возвращает порядок сортировки
     */
    @Override
    public ColumnSort getColumnSort() {
        return columnSort;
    }

    /**
     * Устанавливает новый порядок сортировки
     */
    public void setColumnSort(ColumnSort _columnSort) {
        this.columnSort = _columnSort;
    }

    /**
     * Возвращает приоритетность сортировки
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Устанавливает приоритетность сортировки
     */
    public void setPriority(int _priority) {
        this.priority = _priority;
    }

    /**
     * Сериализация в объект JSON
     */
    public JSONObject exportJSON() throws JSONException {
        JSONObject gsc = new JSONObject();
        gsc.put("id", this.getPropertyModel().getId());
        gsc.put("priority", this.getPriority());
        gsc.put("columnsort", this.getColumnSort());
        gsc.put("label", this.getPropertyModel().getLabel());
        return gsc;
    }

    /**
     * Загрузка значений из объекта JSON
     */
    public void importJSON(JSONObject json) throws JSONException {
        if (json.has("priority")) {
            this.priority = json.optInt("priority", 0);
        }
        if (json.has("columnsort")) {
            this.columnSort = ColumnSort.valueOf(json.optString("columnsort", "UNSORTED"));
        }
    }
}
