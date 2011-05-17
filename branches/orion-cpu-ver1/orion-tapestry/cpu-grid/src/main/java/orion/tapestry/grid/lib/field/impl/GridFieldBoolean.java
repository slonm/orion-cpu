package orion.tapestry.grid.lib.field.impl;

import java.util.ArrayList;
import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementEQ;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementISNOTNULL;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementISNULL;
import orion.tapestry.grid.lib.field.filter.validator.ValidatorRequireBoolean;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * класс для полей типа Boolean
 * @author Gennadiy Dobrovolsky
 */

public class GridFieldBoolean extends GridFieldAbstract<Boolean>  {

    /**
     * Конструктор для самой вероятной конфигурации
     * @param _attributeName  простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldBoolean(String _attributeName) {
        this.init(_attributeName);
        this.setValidator(new ValidatorRequireBoolean());
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
    public GridFieldBoolean(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this.setFilterElementList(this.createFilterElementList());
        this.setValidator(new ValidatorRequireBoolean());
    }

    public GridFieldBoolean() {
        this.setValidator(new ValidatorRequireBoolean());
    }

    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        ArrayList<FilterElementAbstract> list = new ArrayList<FilterElementAbstract>();
        list.add(new FilterElementEQ(this.getAttributeName(), this.getLabel() + " == "));
        list.add(new FilterElementISNOTNULL(this.getAttributeName(), this.getLabel() + " is not null "));
        list.add(new FilterElementISNULL(this.getAttributeName(), this.getLabel() + " is null "));

        for (FilterElementAbstract fe : list) {
            fe.setValidator(this.getValidator());
        }

        return list;
    }

    /**
     * Извлекает из строки "своё" значение в виде строковой константы
     * @param row объект, представляющий строку таблицы
     * @return строка, представляющая значение поля
     */
    @Override
    public String getStringValue(GridRow row) {
        Object val = this.getValue(row);
        if (val == null) {
            return "";
        } else {
            return (val.toString().equalsIgnoreCase("true")?"yes":"no");
        }
    }
}

