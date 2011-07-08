package orion.tapestry.grid.lib.field;

import java.util.List;
import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.field.sort.GridFieldSortType;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * Одна колонка в таблице. Метаинформация.
 * Можно считать контроллером.
 * Соединяет рисование и данные.
 *
 * @param <T> Тип значения в колонке, определяется в реализации метода
 * @author Gennadiy Dobrovolsky
 */
public abstract class GridFieldAbstract<T> {

    /**
     * Идентификатор поля
     */
    private String uid;
    /**
     * Подпись поля
     */
    private String label;
    /**
     * имя атрибута в классе или таблице БД
     * или даже выражение для вычисления значения
     * если NULL, то значение поля вычисляется после выборки из БД
     */
    private String attributeName;
    /**
     * Как показывать соответствующую колонку в таблице
     * (видима или нет, на каком месте)
     * Если NULL, то поле невидимое
     */
    private GridFieldView fieldView;
    /**
     * описание способа сортировки по этому полю
     * (степень важности, сортировать по возрастанию, по убыванию или не сортировать)
     * Если NULL, то сортировать нельзя
     */
    private GridFieldSort fieldSort;
    /**
     * Фильтр для поля
     * Если NULL, то фильтровать
     */
    private List<FilterElementAbstract> fieldFilterElementList;
    /**
     * Обработчик пользовательского ввода
     * (валидатор, преобразователь и т.д.)
     */
    private FieldFilterElementDataType datatype;

    /**
     * Создаёт новый объект с данными для фильтрации
     * @return объект типа FieldFilterAbstract, описывающий фильтр.  Если по этому полю сортировать нельзя, то возвращается NULL
     */
    public abstract List<FilterElementAbstract> createFilterElementList();

    // ================ Конструкторы ===========================================
    /**
     * Простейший конструктор, который автоматически создаёт самую вероятную конфигурацию.
     * т.е. имя атрибута - простое слово, оно же служит уникальным идентификатором поля
     * поле видимое и по нему можно сортировать
     * @param _attributeName идентификатор поля, обязательный параметр
     */
    public GridFieldAbstract(String _attributeName) {
        init(_attributeName);
        this._setFilterElementList(this.createFilterElementList());
    }

    /**
     * Конструктор со всеми параметрами
     * @param _attributeName имя атрибута в таблице БД или даже выражение  (можно передать в конструктор значение NULL, тогда поле не будет участвовать в запросе к базе данных)
     * @param _uid уникальный (в рамках данного списка) идентификатор поля
     * @param _label подпись, человеческое название атрибута, текст, который будет показан человеку
     * @param _fieldView объект, который содержит данные о видимости поля (можно передать в конструктор значение NULL, тогда поле будет невидимым)
     * @param _fieldSort объект, который содержит данные о сортировке строк по данному полю (можно передать в конструктор значение NULL, тогда по полю нельзя будет сортировать)
     */
    public GridFieldAbstract(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        //  Запоминаем все полученные параметры
        init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this._setFilterElementList(this.createFilterElementList());
    }

    public GridFieldAbstract() {
    }

    /**
     * Конструктор со всеми параметрами
     * @param _attributeName имя атрибута в таблице БД или даже выражение  (можно передать в конструктор значение NULL, тогда поле не будет участвовать в запросе к базе данных)
     * @param _uid уникальный (в рамках данного списка) идентификатор поля
     * @param _label подпись, человеческое название атрибута, текст, который будет показан человеку
     * @param _fieldView объект, который содержит данные о видимости поля (можно передать в конструктор значение NULL, тогда поле будет невидимым)
     * @param _fieldSort объект, который содержит данные о сортировке строк по данному полю (можно передать в конструктор значение NULL, тогда по полю нельзя будет сортировать)
     */
    public void init(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        //  Запоминаем все полученные параметры
        this.attributeName = _attributeName;
        this.uid = _uid;
        this.label = _label;
        this.fieldView = _fieldView;
        this.fieldSort = _fieldSort;

    }

    /**
     * Простейший конструктор, который автоматически создаёт самую вероятную конфигурацию.
     * т.е. имя атрибута - простое слово, оно же служит уникальным идентификатором поля
     * поле видимое и по нему можно сортировать
     * @param _attributeName идентификатор поля, обязательный параметр
     */
    public void init(String _attributeName) {
        init(_attributeName,
                _attributeName,
                _attributeName + "-label",
                new GridFieldView()._setUid(_attributeName),
                new GridFieldSort()._setUid(_attributeName)._setSortType(GridFieldSortType.NONE)._setAttributeName(_attributeName));

    }
    // ================ /Конструкторы ==========================================

    // ---------------- get... and set... methods ------------------------------
    public String getUid() {
        return this.uid;
    }

    public GridFieldAbstract setUid(String _uid) {
        this.uid = _uid;
        return this;
    }

    /**
     * Возвращает имя атрибута сущности,
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    public GridFieldAbstract setAttributeName(String _attributeName) {
        this.attributeName = _attributeName;
        return this;
    }

    public String getLabel() {
        return this.label;
    }

    public GridFieldAbstract setLabel(String _label) {
        this.label = _label;
        this.setFilterElementList(createFilterElementList());
        return this;
    }

    /**
     * @return информация о видимости поля (поле=колонка в таблице)
     */
    public GridFieldView getFieldView() {
        return this.fieldView;
    }

    /**
     * Устанавливает новую информация о видимости поля (поле=колонка в таблице)
     * @param _fieldView новая информация о видимости поля
     */
    public void setFieldView(GridFieldView _fieldView) {
        this.fieldView = _fieldView;
    }

    /**
     * Устанавливает новое условие сортировки строк
     * @param _fieldView новая информация о видимости поля
     * @return ссылка на текущий объект для организации цепочек
     */
    public GridFieldAbstract _setFieldView(GridFieldView _fieldView) {
        this.fieldView = _fieldView;
        return this;
    }

    /**
     * @return условие сортировки строк по данному полю
     */
    public GridFieldSort getFieldSort() {
        return this.fieldSort;
    }

    /**
     * Устанавливает новое условие сортировки строк
     * @param _fieldSort новое условие сортировки строк по данному полю
     */
    public void setFieldSort(GridFieldSort _fieldSort) {
        this.fieldSort = _fieldSort;
    }

    /**
     * Устанавливает новое условие сортировки строк
     * @param _fieldSort новое условие сортировки строк по данному полю
     * @return ссылка на текущий объект для организации цепочек
     */
    public GridFieldAbstract _setFieldSort(GridFieldSort _fieldSort) {
        this.fieldSort = _fieldSort;
        return this;
    }

    /**
     * @return объект типа FieldFilterAbstract, описывающий фильтр.  Если по этому полю фильтровать нельзя, то возвращается NULL
     */
    public List<FilterElementAbstract> getFilterElementList() {
        //if (this.fieldFilter == null) {
        //    this.fieldFilter = this.createFilter();
        //}
        return this.fieldFilterElementList;
    }

    /**
     * Устанавливает новый объект с данными для фильтрации
     * @param _fieldFilter новый объект с данными для фильтрации
     */
    public void setFilterElementList(List<FilterElementAbstract> _fieldFilter) {
        this.fieldFilterElementList = _fieldFilter;
    }

    /**
     * Устанавливает новый объект с данными для фильтрации
     * @param _fieldFilter новый объект с данными для фильтрации
     * @return ссылка на текущий объект для организации цепочки
     */
    public GridFieldAbstract _setFilterElementList(List<FilterElementAbstract> _fieldFilter) {
        this.fieldFilterElementList = _fieldFilter;
        return this;
    }

    /**
     * Извлекает из строки "своё" значение в виде строковой константы
     * @param row объект, представляющий строку таблицы
     * @return строка, представляющая значение поля
     */
    public String getStringValue(GridRow row) {
        Object val = this.getValue(row);
        if (val == null) {
            return "";
        } else {
            return val.toString();
        }
    }

    /**
     * Извлекает из строки "своё" значение, преобразованное к правильному типу
     * @param row объект, представляющий строку таблицы
     * @return строка, представляющая значение поля
     */
    public T getValue(GridRow row) {
        T val = (T) row.getValue(this.getUid());
        return val;
    }

    /**
     * @return Объект-валидатор пользовательского ввода
     */
    public FieldFilterElementDataType getDatatype() {
        return this.datatype;
    }

    /**
     * Устанавливает новый объект в качестве валидатора
     * @param _validator объект-валидатор пользовательского ввода
     */
    public void setDatatype(FieldFilterElementDataType _validator) {
        this.datatype = _validator;
        if (this.fieldFilterElementList != null) {
            for (FilterElementAbstract fe : this.fieldFilterElementList) {
                fe.setDatatype(this.datatype);
            }
        }
    }
    // ---------------- /get... and set... methods -----------------------------
}
