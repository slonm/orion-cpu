package orion.tapestry.grid.lib.model.view;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * Контейнер для свойств, управляющих отображением колонок
 * (порядок отображения, видимость)
 * @author dobro
 */
public class GridPropertyViewModel {

    /**
     * список свойств видимости
     */
    SortedSet<GridPropertyView> viewProperties = new TreeSet<GridPropertyView>(new GridPropertyViewComparator());

    /**
     * Модель, которую надо отображать
     */
    private GridBeanModel model;
    
    public GridPropertyViewModel(GridBeanModel _model) {
        model=_model;
        for (Object m : _model.getPropertyNames()) {
            GridPropertyView vi = ((GridPropertyModelInterface) _model.get(m.toString())).getGridPropertyView();
            if (vi != null) {
                viewProperties.add(vi);
            }
        }
    }

    /**
     * Сериализация в строку формата JSON
     */
    public String exportJSONString() throws JSONException {
        JSONObject json = new JSONObject();
        for (GridPropertyView gpv : this.viewProperties) {
            GridPropertyModelInterface vi = gpv.getGridPropertyModel();
            if (vi != null) {
                String id = vi.getId();
                json.put(id, gpv.exportJSON());
            }
        }
        return json.toString();
    }

    /**
     * Загрузка значений из строки формата JSON
     */
    public void loadJSON(String JSONString) throws JSONException {
        JSONObject json = new JSONObject(JSONString);
        for (String name : JSONObject.getNames(json)) {
        }
    }

    /**
     * Устанавливаем значения по умолчанию
     */
    public void reset() {
        for (GridPropertyView gpv : this.viewProperties) {
            gpv.setIsVisible(true);
            gpv.setOrdering(0);
            gpv.setWidth(200);
        }
    }

    /**
     * Возвращает список всех условий отображения
     */
    public List<GridPropertyView> getGridViewProperties() {
        return new ArrayList<GridPropertyView>(this.viewProperties);
    }

    /**
     * По идентификатору колонки возвращает правило отображения
     */
    public GridPropertyView getColumnSort(String columnId) {
        for (GridPropertyView gpv : this.viewProperties) {
            if (gpv.getGridPropertyModel().getId().equals(columnId)) {
                return gpv;
            }
        }
        return null;
    }

    public String toString() {
        try {
            return exportJSONString();
        } catch (JSONException ex) {
            // TODO: add logger here
        }
        return "";
    }
    
    public GridBeanModel getModel(){
        return model;
    }
}
