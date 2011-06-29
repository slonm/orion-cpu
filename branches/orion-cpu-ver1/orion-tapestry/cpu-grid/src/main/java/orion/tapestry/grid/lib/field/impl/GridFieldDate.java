package orion.tapestry.grid.lib.field.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FieldFilterElementGUIType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.filter.impl.*;
import orion.tapestry.grid.lib.field.filter.datatype.FieldFilterElementDate;
import orion.tapestry.grid.lib.field.filter.datatype.FieldFilterElementList;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;

/**
 * Класс, который описывает поле типа java.util.Date
 * @author Gennadiy Dobrovolsky
 */
public class GridFieldDate extends GridFieldAbstract<Date> {

    /**
     * Конструктор для самой вероятной конфигурации
     * @param _attributeName  простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldDate(String _attributeName) {
        this.init(_attributeName);
        this.setDatatype(new FieldFilterElementDate());
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
    public GridFieldDate(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this.setDatatype(new FieldFilterElementDate());
        this.setFilterElementList(this.createFilterElementList());
    }

    public GridFieldDate() {
        this.setDatatype(new FieldFilterElementDate());
    }

    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        //System.out.println("Date ... ");
        ArrayList<FilterElementAbstract> list = new ArrayList<FilterElementAbstract>();
        list.add(new FilterElementEQ(this.getAttributeName(), this.getLabel() + " == "));
        list.add(new FilterElementGE(this.getAttributeName(), this.getLabel() + " >= "));
        list.add(new FilterElementGT(this.getAttributeName(), this.getLabel() + " > "));
        list.add(new FilterElementLE(this.getAttributeName(), this.getLabel() + " <= "));
        list.add(new FilterElementLT(this.getAttributeName(), this.getLabel() + " < "));
        list.add(new FilterElementNEQ(this.getAttributeName(), this.getLabel() + " != "));

        // устанавливаем особенный тип элемента формы
        for (FilterElementAbstract fe : list) {
            fe.setType(FieldFilterElementGUIType.DATE);
        }

        list.add(new FilterElementISNOTNULL(this.getAttributeName(), this.getLabel() + " is not null "));
        list.add(new FilterElementISNULL(this.getAttributeName(), this.getLabel() + " is null "));

        for (FilterElementAbstract fe : list) {
            fe.setDatatype(this.getDatatype());
        }

        return list;
    }
}
