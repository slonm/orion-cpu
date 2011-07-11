package orion.tapestry.grid.lib.field.sort;

/**
 * Свойства, которые управляют сортировкой строк
 * Этот класс - простой контейнер.
 * @author Gennadiy Dobrovolsky 
 */
public class GridFieldSort {

    /**
     * Номер по порядку в списке сортировки
     */
    private int ordering=0;


    /**
     * Как сортировать поля
     */
    private GridFieldSortType sortType=GridFieldSortType.NONE;


    /**
     * UID поля
     */
    private String uid;

    /**
     * название поля
     */
    private String label;

    /**
     * имя атрибута сущности или выражение
     */
    private String attributeName;

    /**
     * Устанавливаем способ сортировки
     * @param _sortType новое значение сортируемости
     */
    public void setSortType(GridFieldSortType _sortType) {
        this.sortType = _sortType;
    }
    /**
     * Устанавливаем способ сортировки
     * @param _sortType новое значение сортируемости
     * @return ссылка на текущий объект
     */
    public GridFieldSort _setSortType(GridFieldSortType _sortType) {
        this.sortType = _sortType;
        return this;
    }
    /**
     * Константы для типов сортировки
     * (используются компонентой для построения  формы управления сортировкой)
     */
    public GridFieldSortType getSortTypeValueAsc() { return GridFieldSortType.ASC; }
    public GridFieldSortType getSortTypeValueDesc() { return GridFieldSortType.DESC; }
    public GridFieldSortType getSortTypeValueNone() { return GridFieldSortType.NONE; }

    /**
     * Узнаём способ сортировки
     * @return признак сортируемости поля
     */
    public GridFieldSortType getSortType() {
        return this.sortType;
    }

    /**
     * Устанавливаем важность поля при сортировке строк,
     * поля с большим номером будут иметь больший вес при сортировке
     * (одно поле=одна колонка в таблице)
     * @param _ordering новый номер поля по порядку
     */
    public void setOrdering(int _ordering) {
        this.ordering = _ordering;
    }
    /**
     * Устанавливаем важность поля при сортировке строк,
     * поля с большим номером будут иметь больший вес при сортировке
     * (одно поле=одна колонка в таблице)
     * @param _ordering новый номер поля по порядку
     * @return ссылка на текущий объект
     */
    public GridFieldSort _setOrdering(int _ordering) {
        this.ordering = _ordering;
        return this;
    }

    /**
     * Узнаём важность поля при сортировке строк,
     * поля с большим номером будут иметь больший вес при сортировке
     * (одно поле=одна колонка в таблице)
     * @return номер поля по порядку
     */
    public int getOrdering() {
        return this.ordering;
    }

    /**
     * Устанавливаем новое значение UID для поля
     * @param _uid новое значение UID
     */
    public void setUid(String _uid) {
        this.uid = _uid;
    }

    /**
     * Устанавливаем новое значение UID для поля
     * @return ссылка на текущий объект
     */
    public GridFieldSort _setUid(String _uid) {
        this.uid = _uid;
        return this;
    }
    /**
     * Узнаём значение UID для поля
     * @return значение UID
     */
    public String getUid() {
        return this.uid;
    }



    /**
     * Устанавливаем новое значение UID для поля
     * @param _attributeName новое значение UID
     */
    public void setAttributeName(String _attributeName) {
        this.attributeName = _attributeName;
    }

    /**
     * Устанавливаем новое значение UID для поля
     * @return ссылка на текущий объект
     */
    public GridFieldSort _setAttributeName(String _attributeName) {
        this.attributeName = _attributeName;
        return this;
    }
    /**
     * Узнаём значение UID для поля
     * @return значение UID
     */
    public String getAttributeName() {
        return this.attributeName;
    }



    /**
     * Устанавливаем новое значение текстовой метки для поля
     * @param _label новое значение текстовой метки
     */
    public void setLabel(String _label) {
        this.label = _label;
    }

    public GridFieldSort _setLabel(String _label) {
        this.label = _label;
        return this;
    }

    /**
     * Узнаём значение UID для поля
     * @return значение UID
     */
    public String getLabel() {
        return this.label;
    }
}
