package orion.tapestry.grid.lib.model.view;

import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * Свойства, которые управляют видимостью одной колонки
 * @author Gennadiy Dobrovolsky 
 */
public class GridPropertyView {

    /**
     * Видимая колонка или нет
     */
    private boolean isVisible = true;

    /**
     * Номер по порядку
     */
    private int ordering = 0;

    /**
     * Модель, которой принадлежит свойство
     */
    private GridPropertyModelInterface gridPropertyModel;

    /**
     * название поля
     */
    private String label;


    /**
     * Ширина колонки, px
     */
    private int width = 200;


    public GridPropertyView(GridPropertyModelInterface _gridPropertyModel, int _ordering){
        this.gridPropertyModel=_gridPropertyModel;
        this.ordering=_ordering;
    }

    /**
     * Устанавливаем видимость поля
     * @param _isVisible новое значение видимости
     */
    public void setIsVisible(boolean _isVisible) {
        this.isVisible = _isVisible;
    }

    /**
     * Устанавливаем ширину колонки
     * @param _width новая ширина колонки
     */
    public void setWidth(int _width) {
        this.width = _width;
    }


    /**
     * @return ширина колонки
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Узнаём видимость поля
     * @return признак видимости поля
     */
    public boolean getIsVisible() {
        return this.isVisible;
    }

    /**
     * Устанавливаем номер поля по порядку,
     * поля с меньшим номером будут показаны левее
     * (одно поле=одна колонка в таблице)
     * @param _ordering новый номер поля по порядку
     */
    public void setOrdering(int _ordering) {
        this.ordering = _ordering;
    }


    /**
     * Узнаём номер поля по порядку, 
     * поля с меньшим номером будут показаны левее
     * (одно поле=одна колонка в таблице)
     * @return номер поля по порядку
     */
    public int getOrdering() {
        return this.ordering;
    }

    /**
     * Связать правило отображение с новой моделью
     * @param _gridPropertyModel новая модель
     */
    public void setGridPropertyModel(GridPropertyModelInterface _gridPropertyModel) {
        this.gridPropertyModel = _gridPropertyModel;
    }


    /**
     * Возвращает модель, к которой привязано это правило отображения
     * @return модель
     */
    public GridPropertyModelInterface getGridPropertyModel() {
        return this.gridPropertyModel;
    }

    /**
     * Узнаём значение UID для поля
     * @return значение UID
     */
    public String getLabel() {
        return  this.gridPropertyModel.getLabel();
    }

     /**
     * Сериализация в формат JSON
     */
    public JSONObject exportJSON() throws JSONException {
        JSONObject gsc = new JSONObject();
        gsc.put("id", this.gridPropertyModel.getId());
        gsc.put("isvisible", this.isVisible);
        gsc.put("ordering", this.ordering);
        gsc.put("width", this.width);
        gsc.put("label", this.gridPropertyModel.getLabel());
        return gsc;
    }

    /**
     * Загрузка значений из формата JSON
     */
    public void importJSON(JSONObject json) throws JSONException {
        if(json.has("isvisible")){
            this.isVisible=json.optBoolean("isvisible", true);
        }
        if(json.has("ordering")){
            this.ordering=json.optInt("ordering", 0);
        }
        if(json.has("width")){
            this.width=json.optInt("width", 200);
        }
    }
}
