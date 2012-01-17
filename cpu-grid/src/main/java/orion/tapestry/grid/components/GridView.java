package orion.tapestry.grid.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.json.JSONException;
import orion.tapestry.grid.lib.model.view.GridPropertyViewModel;

/**
 * Редактор способа отображения таблицы (видимость, ширина, порядок колонок)
 * @author dobro
 */
@Import(library = {
    "${tapestry.scriptaculous}/prototype.js",
    "${tapestry.scriptaculous}/dragdrop.js",
    "gridview.js"},
stylesheet = {"gridview.css"})
public class GridView {

    /**
     * Источник метаданных таблицы
     * Обязательный параметр компоненты,
     * входные данные
     */
    @Parameter(principal = true, required = true)
    @Property
    private GridPropertyViewModel gridPropertyViewModel;
    /**
     * Закодированные в строку формата JSON данные о видимости колонок
     */
    @Persist
    @Property
    private String gridPropertyViewJSON;

    void setupRender() {
        if (gridPropertyViewJSON == null) {
            try {
                if (gridPropertyViewModel == null) {
                    gridPropertyViewJSON = "{}";
                } else {
                    gridPropertyViewJSON = gridPropertyViewModel.exportJSONString();
                }
            } catch (JSONException ex) {
                gridPropertyViewJSON = "{}";
            }
        }
    }
//
//    public  void setGridPropertyViewJSON(String newValue){
//        this.gridPropertyViewJSON=newValue;
//    }
//    public String getGridPropertyViewJSON(){
//        return this.gridPropertyViewJSON;
//    }
}
