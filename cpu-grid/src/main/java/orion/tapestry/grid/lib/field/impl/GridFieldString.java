package orion.tapestry.grid.lib.field.impl;

import java.util.ArrayList;
import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.*;
import orion.tapestry.grid.lib.field.filter.impl.*;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;

/**
 * Класс, который описывает поле типа String
 * @author Gennadiy Dobrovolsky
 */
public class GridFieldString extends GridFieldAbstract<String> {

    /**
     * Конструктор самой вероятной конфигурации
     * @param _attributeName простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldString(String _attributeName) {
        this.init(_attributeName);
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
    public GridFieldString(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this.setFilterElementList(this.createFilterElementList());
    }

    public GridFieldString() {
    }

    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        ArrayList<FilterElementAbstract> list = new ArrayList<FilterElementAbstract>();
        list.add(new FilterElementCONTAINS( this.getAttributeName(), this.getLabel()+" contains "));
        list.add(new FilterElementEQ(       this.getAttributeName(), this.getLabel()+" == "));
        list.add(new FilterElementGE(       this.getAttributeName(), this.getLabel()+" >= "));
        list.add(new FilterElementGT(       this.getAttributeName(), this.getLabel()+" > "));
        list.add(new FilterElementIN(       this.getAttributeName(), this.getLabel()+" in "));
        list.add(new FilterElementISNOTNULL(this.getAttributeName(), this.getLabel()+" is not null "));
        list.add(new FilterElementISNULL(   this.getAttributeName(), this.getLabel()+" is null "));
        list.add(new FilterElementLE(       this.getAttributeName(), this.getLabel()+" <="));
        list.add(new FilterElementLIKE(     this.getAttributeName(), this.getLabel()+" like "));
        list.add(new FilterElementLT(       this.getAttributeName(), this.getLabel()+" < "));
        list.add(new FilterElementNEQ(      this.getAttributeName(), this.getLabel()+" != "));
        return list;
    }
}
