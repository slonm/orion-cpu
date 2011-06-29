package orion.tapestry.grid.lib.field.impl;

import java.util.ArrayList;
import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementEQ;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementISNOTNULL;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementISNULL;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementText;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * Пример класса - описания типа колонки
 * при написании своего класса надо заменить SampleDataType своим типом данных
 * после этого добавляем созданный класс в конфигурацию:
 *
 *     public static void contributeTypeMap(MappedConfiguration<String, Class<? extends GridFieldAbstract>> configuration) {
 *         configuration.add(SampleDataType.class.getName(), GridFieldSample.class);
 *     }
 * 
 * @author Gennadiy Dobrovolsky
 */

public class GridFieldSample extends GridFieldAbstract<SampleDataType>  {

    /**
     * Конструктор для самой вероятной конфигурации
     * @param _attributeName  простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldSample(String _attributeName) {
        this.init(_attributeName);

        // назначаем объект для проверки/преобразования типа данных
        this.setDatatype(new DatatypeSample());

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
    public GridFieldSample(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this.setFilterElementList(this.createFilterElementList());
        // назначаем объект для проверки/преобразования типа данных
        this.setDatatype(new DatatypeSample());
    }

    public GridFieldSample() {
        // назначаем объект для проверки/преобразования типа данных
        this.setDatatype(new DatatypeSample());
    }


    // создание фильтров для данного типа
    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        ArrayList<FilterElementAbstract> list = new ArrayList<FilterElementAbstract>();
        list.add(new FilterElementEQ(this.getAttributeName(), this.getLabel() + " == "));
        list.add(new FilterElementISNOTNULL(this.getAttributeName(), this.getLabel() + " is not null "));
        list.add(new FilterElementISNULL(this.getAttributeName(), this.getLabel() + " is null "));

        for (FilterElementAbstract fe : list) {
            fe.setDatatype(this.getDatatype());
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


/**
 * Обработчик входных данных
 */
class DatatypeSample implements FieldFilterElementDataType<SampleDataType> {

    // проверяем входные данные
    @Override
    public boolean isValid(String value) {
        return value.matches("yes|no|false|true|0|1");
    }

    // преобразуем данные из строки
    @Override
    public SampleDataType fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new SampleDataType(value);
        }catch(NumberFormatException e){
            return null;
        }
    }

    // функция на языке JavaScript для проверки данных
    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }
}


/**
 * пример типа данных
 */
class SampleDataType{
    public SampleDataType(String value){
    }
}


/**
 * Элементарный фильтр, который будет использоватся при выборке данных
 * и получать значения из тестового поля
 * @author Gennadiy Dobrovolsky
 */
class FilterElementKnowledgeAreaOrTrainingDirectionContains extends FilterElementText {

    public FilterElementKnowledgeAreaOrTrainingDirectionContains(String newFieldName, String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName + "CONTAINS");
        this.setLabel(newLabel);
    }

    @Override
    public <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException {
        // элемент должен быть активным
        if (!isActive) {
            return false;
        }

        // значение должно существовать
        if (value == null) {
            return false;
        }

        // используем валидатор для проверки данных
        Object checkedValue;
        if (datatype != null) {
            checkedValue = datatype.fromString(value.toString());
            if (checkedValue == null) {
                return false;
            }
        }else{
            checkedValue=value;
        }
        restriction.constField(this.fieldName);
        restriction.constValue(checkedValue);
        restriction.contains();
        return true;
    }
}
