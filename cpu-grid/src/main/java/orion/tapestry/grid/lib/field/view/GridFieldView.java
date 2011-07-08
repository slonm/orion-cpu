package orion.tapestry.grid.lib.field.view;

/**
 * Свойства, которые управляют видимостью колонок
 * Этот класс - простой контейнер.
 * @author Gennadiy Dobrovolsky 
 */
public class GridFieldView {

    /**
     * Видимая колонка или нет
     */
    private boolean isVisible = true;
    /**
     * Номер по порядку
     */
    private int ordering = 0;
    /**
     * UID поля
     */
    private String uid;
    /**
     * название поля
     */
    private String label;

    /**
     * можно ли управлять видимостью
     */

    /**
     * Устанавливаем видимость поля
     * @param _isVisible новое значение видимости
     */
    public void setIsVisible(boolean _isVisible) {
        this.isVisible = _isVisible;
    }

    /**
     * Устанавливаем видимость поля
     * @param _isVisible новое значение видимости
     * @return ссылка на текущий объект для составления цепочек
     */
    public GridFieldView _setIsVisible(boolean _isVisible) {
        this.isVisible = _isVisible;
        return this;
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
     * Устанавливаем номер поля по порядку,
     * поля с меньшим номером будут показаны левее
     * (одно поле=одна колонка в таблице)
     * @param _ordering новый номер поля по порядку
     * @return  ссылка на текущий объект для составления цепочек
     */
    public GridFieldView _setOrdering(int _ordering) {
        this.ordering = _ordering;
        return this;
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
     * Устанавливаем новое значение UID для поля
     * @param _uid новое значение UID
     */
    public void setUid(String _uid) {
        this.uid = _uid;
    }

    public GridFieldView _setUid(String _uid) {
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
     * Устанавливаем новое значение текстовой метки для поля
     * @param _uid новое значение UID
     */
    public void setLabel(String _label) {
        this.label = _label;
    }

    public GridFieldView _setLabel(String _label) {
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
