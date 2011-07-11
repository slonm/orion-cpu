package orion.tapestry.grid.lib.field.impl;

import orion.tapestry.grid.lib.field.filter.datatype.FieldFilterElementFloat;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * Класс, который описывает поле типа Float
 * @author Gennadiy Dobrovolsky
 */
public class GridFieldNumberFloat extends GridFieldNumberAbstractFloat<Float> {

    /**
     * Конструктор для самой вероятной конфигурации
     * @param _attributeName  простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldNumberFloat(String _attributeName) {
        this.init(_attributeName);
        this.setDatatype(new FieldFilterElementFloat());
        this.setFilterElementList(this.createFilterElementList());
    }

    /**
     * Конструктор с максимальным количеством параметров
     * @param _attributeName имя атрибута в таблице БД или даже выражение  (можно передать в конструктор значение NULL, тогда поле не будет участвовать в запросе к базе данных)
     * @param _uid уникальный (в рамках данного списка) идентификатор поля
     * @param _label подпись, человеческое название атрибута, текст, который будет показан человеку
     * @param _fieldView объект, который содержит данные о видимости поля (можно передать в конструктор значение NULL, тогда поле будет невидимым)
     * @param _fieldSort объект, который содержит данные о сортировке строк по данному полю (можно передать в конструктор значение NULL, тогда по полю нельзя будет сортировать)
     */
    public GridFieldNumberFloat(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this.setDatatype(new FieldFilterElementFloat());
        this.setFilterElementList(this.createFilterElementList());
        ;
    }

    public GridFieldNumberFloat() {
        this.setDatatype(new FieldFilterElementFloat());
    }

    @Override
    public String getStringValue(GridRow row) {
        Float val = this.getValue(row);
        if (val == null) {
            return "";
        } else {
            return val.toString();
        }
    }

    @Override
    public Float getValue(GridRow row) {
        Object val = row.getValue(this.getUid());
        if (val instanceof Float) {
            return new Float(((Float) val));
        }
        if (val instanceof Float) {
            return ((Float) val);
        }
        return null;
    }
}
