package orion.tapestry.grid.lib.field.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.rows.GridRow;


/**
 * Пример класса - описания типа колонки
 * при написании своего класса надо заменить SampleDataType своим типом данных
 * после этого добавляем созданный класс в конфигурацию:
 *
 *     public static void contributeTypeMap(MappedConfiguration<String, Class<? extends GridFieldAbstract>> configuration) {
 *         configuration.add(GridFieldSample.class.getName(), GridFieldSample.class);
 *     }
 * 
 * @author Gennadiy Dobrovolsky
 */

public class GridFieldKnowledgeAreaOrTrainingDirection extends GridFieldAbstract<KnowledgeAreaOrTrainingDirection>  {
    /**
     * Logger class
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GridFieldKnowledgeAreaOrTrainingDirection.class);
    /**
     * Конструктор для самой вероятной конфигурации
     * @param _attributeName  простое имя атрибута таблицы в БД или класса-сущности в ORM
     */
    public GridFieldKnowledgeAreaOrTrainingDirection(String _attributeName) {
        this.init(_attributeName);

        // назначаем объект для проверки/преобразования типа данных
        //this.setValidator(new ValidatorSample());
        this.setValidator(null);

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
    public GridFieldKnowledgeAreaOrTrainingDirection(String _attributeName, String _uid, String _label, GridFieldView _fieldView, GridFieldSort _fieldSort) {
        this.init(_attributeName, _uid, _label, _fieldView, _fieldSort);
        this.setFilterElementList(this.createFilterElementList());
        // назначаем объект для проверки/преобразования типа данных
        //this.setValidator(new ValidatorSample());
        this.setValidator(null);
    }

    public GridFieldKnowledgeAreaOrTrainingDirection() {
        // назначаем объект для проверки/преобразования типа данных
        //this.setValidator(new ValidatorSample());
        this.setValidator(null);
    }


    // создание фильтров для данного типа
    @Override
    public List<FilterElementAbstract> createFilterElementList() {
        ArrayList<FilterElementAbstract> list = new ArrayList<FilterElementAbstract>();
        list.add(new FilterElementKnowledgeAreaOrTrainingDirection(this.getAttributeName(), this.getLabel() + " contains "));
        //list.add(new FilterElementISNOTNULL(this.getAttributeName(), this.getLabel() + " is not null "));
        //list.add(new FilterElementISNULL(this.getAttributeName(), this.getLabel() + " is null "));
        LOG.info("xxxxxxxxxxxxxxxx");
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
            return (val.toString());
        }
    }

}



//class ValidatorKnowledgeAreaOrTrainingDirection implements FieldFilterElementValidator<KnowledgeAreaOrTrainingDirection> {
//
//    // проверяем входные данные
//    @Override
//    public boolean isValid(String value) {
//        return true;
//    }
//
//    // преобразуем данные из строки для применения в условии фильтрации
//    @Override
//    public KnowledgeAreaOrTrainingDirection fromString(String value) {
//        if (!isValid(value)) return null;
//        try{
//            return new KnowledgeAreaOrTrainingDirection();
//        }catch(NumberFormatException e){
//            return null;
//        }
//    }
//
//    // функция на языке JavaScript для проверки данных
//    @Override
//    public String getJSValidator() {
//        return "null";
//    }
//}


///**
// * пример типа данных
// */
//class SampleDataType{
//    public SampleDataType(String value){
//    }
//}

