package orion.tapestry.grid.lib.model.filter;

import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Атомарный элемент фильтра.
 * Принимает и обрабатывает данные из одного элемента в форме поиска.
 * @author Gennadiy Dobrovolsky
 */
public abstract class GridFilterAbstract {

    /**
     * Текстовая метка фильтра
     * например "больше чем", "не равно" и т.д.
     */
    private String label;
    /**
     * уникальный идентификатор элемента
     */
    private String uid;
    /**
     * тип элемента формы
     */
    private GridFilterGUIType GUIType;
    /**
     * Валидатор
     */
    protected GridFilterDataType dataType;
    /**
     * Модель свойства, для которого предназначено это условие фильтрации
     */
    protected GridPropertyModelInterface model;
    /**
     * имя поля, на которое накладывается фильтр
     */
    protected String fieldName;

    public GridFilterAbstract() {
        this.model = null;
        this.uid = null;
        this.fieldName = null;
    }

    public GridFilterAbstract(GridPropertyModelInterface _model) {
        this.model = _model;
        this.uid = this.model.getId();
        this.fieldName = this.model.getPropertyName();
    }

    public GridFilterAbstract(GridPropertyModelInterface _model, String _uid, String _fieldName) {
        this.model = _model;
        this.uid = _uid;
        this.fieldName = _fieldName;
    }

    /**
     * Метод изменяет условие отбора, которое будет передано в источник данных
     * @param <T> класс условия отбора
     * @param restriction объект "Редактор условия отбора"
     * @param value Параметр фильтра, чаше всего это введённая пользователем строка
     * @param isActive активный фильтр или нет
     * @param nChildren  Количество активных под-узлов, используется для элементов типа И, ИЛИ, НЕ
     * @return true, если этот элемент изменил условие фильтрации, и false, если условие не изменялось
     * @throws RestrictionEditorException если невозможно изменить условие отбора
     */
    public abstract <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException;

    /**
     * @return возвращает текстовую метку элемента формы c названием атрибута
     */
    public String getLabel() {
        return String.format("%s %s", this.model.getLabel(), this.label);
    }

    /**
     * @param newLabel устанавливает текстовую метку элемента формы
     */
    public void setLabel(String newLabel) {
        this.label = newLabel;
    }

    /**
     * @return возвращает уникальный идентификатор элемента
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * @param newUid новый уникальный идентификатор элемента формы
     */
    public void setUid(String newUid) {
        this.uid = newUid;
    }

    /**
     * @return тип элемента формы
     */
    public GridFilterGUIType getGUIType() {
        return this.GUIType;
    }

    /**
     * @param newType новый тип элемента формы
     */
    public void setGUIType(GridFilterGUIType newType) {
        this.GUIType = newType;
    }

    /**
     * @return Объект-валидатор пользовательского ввода
     */
    public GridFilterDataType getDataType() {
        return this.dataType;
    }

    /**
     * Устанавливает новый объект в качестве валидатора
     * @param _datatype объект-валидатор пользовательского ввода
     */
    public void setDataType(GridFilterDataType _datatype) {
        this.dataType = _datatype;
    }

    /**
     * возвращает модель, для которой предназначен этот фильтр
     */
    public GridPropertyModelInterface getGridPropertyModel() {
        return this.model;
    }

    /**
     * устанавливает модель, для которой предназначен этот фильтр
     */
    public void setGridPropertyModel(GridPropertyModelInterface _model) {
        this.model = _model;
    }

    /**
     * Строка JSON, которая представляет javascript-конструктор для этого элемента формы
     * uid - уникальный идентификатор этого элемента
     * label - название элемента
     * validator - имя валидатора или null
     * guitype - название типа элемента формы
     */
    public String getJSONString() {
        return getJSONObject().toString();
    }

    public JSONObject getJSONObject() {
        // Строка JSON, которая представляет javascript-конструктор для этого элемента формы
        // typeName - уникальный идентификатор этого элемента
        // label - название элемента
        // validator - имя валидатора или null
        // guitype - название типа элемента формы
        JSONObject result = new JSONObject();
        try {
            // config.typeName
            result.put("typeName", this.getUid());

            // config.isactive
            result.put("isactive", "1");

            // config.uid
            result.put("uid", "");

            // config.value
            result.put("value", "");

            // config.guitype
            result.put("guitype", this.getGUIType());

            // config.label
            result.put("label", this.getLabel());

            // config.validator
            String validator;
            GridFilterDataType dt = this.getDataType();
            validator = (dt != null) ? dt.getJSValidator() : "";
            result.put("validator", validator == null ? "" : validator);

        } catch (JSONException ex) {
            // TODO: add logger here
        }
        return result;
    }
}
