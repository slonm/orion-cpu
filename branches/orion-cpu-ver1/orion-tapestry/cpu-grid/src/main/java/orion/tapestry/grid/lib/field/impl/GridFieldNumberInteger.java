package orion.tapestry.grid.lib.field.impl;

import orion.tapestry.grid.lib.field.filter.datatype.FieldFilterElementInteger;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;

/**
 * Класс, который описывает поле типа Byte
 * @author Gennadiy Dobrovolsky
 */
public class GridFieldNumberInteger extends GridFieldNumberAbstractInt<Integer> {

    /**
     * Конструктор для самой вероятной конфигурации
     * @param _attributeName  простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldNumberInteger(String _attributeName) {
        this.init(_attributeName);
        this._setFilterElementList(this.createFilterElementList());
        this.setDatatype(new FieldFilterElementInteger());
    }

    /**
     * Конструктор с максимальным количеством параметров
     * @param _attributeName имя атрибута в таблице БД или даже выражение  (можно передать в конструктор значение NULL, тогда поле не будет участвовать в запросе к базе данных)
     * @param _uid уникальный (в рамках данного списка) идентификатор поля
     * @param _label подпись, человеческое название атрибута, текст, который будет показан человеку
     * @param _fieldView объект, который содержит данные о видимости поля (можно передать в конструктор значение NULL, тогда поле будет невидимым)
     * @param _fieldSort объект, который содержит данные о сортировке строк по данному полю (можно передать в конструктор значение NULL, тогда по полю нельзя будет сортировать)
     */
    public GridFieldNumberInteger(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this._setFilterElementList(this.createFilterElementList());
        this.setDatatype(new FieldFilterElementInteger());
    }

    public GridFieldNumberInteger() {
        this.setDatatype(new FieldFilterElementInteger());
    }
}
