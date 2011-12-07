package orion.tapestry.grid.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import orion.tapestry.grid.lib.model.sort.GridSortModelImpl;

/**
 * компонента для редактирования условий фильтрации
 * @author dobro
 */
@Import(library = {
    "${tapestry.scriptaculous}/prototype.js",
    "${tapestry.scriptaculous}/dragdrop.js",
    "gridsort.js"},
stylesheet = {"gridsort.css"})
public class GridSort {

    /**
     * Источник метаданных таблицы
     * Обязательный параметр компоненты,
     * входные данные
     */
    @Parameter(principal = true, required = true)
    private GridSortModelImpl gridSortModel;
    /**
     * Здесь хранится закодированный в формат JSON список правил сортировки
     */
    @Persist
    private String gridSortJSON;

    void setupRender() {
        if (gridSortJSON == null) {
            // устанавливаем начальное значение 
            if (gridSortModel == null) {
                gridSortJSON = "{}";
            } else {
                gridSortJSON = gridSortModel.exportJSONString();
            }

        }
    }

    /**
     * Метод для установки нового значения
     * закодированного в формат JSON
     * списка правил сортировки
     */
    public void setGridSortJSON(String f) {
        this.gridSortJSON = f;
    }

    /**
     * Метод для возвращает значение
     * закодированного в формат JSON
     * списка правил сортировки
     */
    public String getGridSortJSON() {
        return this.gridSortJSON;
    }

    public GridSortModelImpl getGridSortModel() {
        return this.gridSortModel;
    }

    public void setGridSortModel(GridSortModelImpl model) {
        this.gridSortModel = model;
    }
}
