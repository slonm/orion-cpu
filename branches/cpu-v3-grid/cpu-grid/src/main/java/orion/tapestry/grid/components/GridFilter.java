package orion.tapestry.grid.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;

/**
 * Редактор условия фильтрации
 * @author dobro
 */
@Import(library = {
    "${tapestry.scriptaculous}/prototype.js",
    "${tapestry.scriptaculous}/dragdrop.js",
    "gridfilter.js", "dateselector.js"},
stylesheet = {"gridfilter.css"})
public class GridFilter {

    /**
     * Источник метаданных таблицы
     * Обязательный параметр компоненты,
     * входные данные
     */
    @Parameter(principal = true, required = true)
    @Property
    private GridFilterModel gridFilterModel;
    @Persist
    private String gridFilterJSON;
    /**
     * Сохранённое название типа записи
     */
    @Persist
    private String savedBeanName;

    public String getElementBuildersJSONString() {
        if (gridFilterModel == null) {
            return "{}";
        } else {
            return gridFilterModel.getElementBuildersJSONString();
        }

    }

    void setupRender() {
        String currentBeanName = gridFilterModel.getModel().getBeanType().getName();
        // в первый раз просто запоминаем название типа
        if (savedBeanName == null) {
            savedBeanName = currentBeanName;
        } else {
            // проверяем, не изменился ли класс
            // и если изменился, то очищаем все предыдущие свойства
            if (!savedBeanName.equals(currentBeanName)) {
                savedBeanName = currentBeanName;
                gridFilterJSON = null;
            }
        }
        if (gridFilterJSON == null) {
            gridFilterJSON = "{}";
        }
    }

    public void setGridFilterJSON(String f) {
        this.gridFilterJSON = f;
    }

    /**
     * Закодированное в виде JSON условие фильтрации
     */
    public String getGridFilterJSON() {
        return this.gridFilterJSON;
    }
}
