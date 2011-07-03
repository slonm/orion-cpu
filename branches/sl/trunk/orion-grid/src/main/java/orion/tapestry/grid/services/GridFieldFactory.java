package orion.tapestry.grid.services;

import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.field.sort.GridFieldSortType;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import java.util.*;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import orion.tapestry.grid.lib.field.impl.*;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.slf4j.Logger;
import orion.tapestry.grid.lib.field.GridFieldAbstract;

/**
 * Класс конструирует список полей по классу-сущности
 * @author Gennadiy Dobrovolsky 
 */
public class GridFieldFactory {

    private final Logger log;
    private final PropertyAccess propertyAccess;

    public GridFieldFactory(Logger log, PropertyAccess propertyAccess) {
        this.log = log;
        this.propertyAccess = propertyAccess;
    }
    
    /**
     * Основной метод для генерации списка полей для модели данных
     *
     * @param forClass класс сущности, который описывает атрибуты
     * @param configuration конфигурация, содержащая отображение тип данных=> тип колонки
     * @param messages текстовые сообщения для интерфейса, использует стандартный механизм локализации Tapestry
     * @return список полей, которые описывают колонки в таблице
     */
    public List<GridFieldAbstract> getFields(
            Class forClass,
            Map<String, Class> configuration) {

        // этот список будем возвращать из метода
        ArrayList<GridFieldAbstract> fields = new ArrayList<GridFieldAbstract>();

        ClassPropertyAdapter cpa = propertyAccess.getAdapter(forClass);


        // Временныя переменные для цикла
        Class attributetype;
        GridFieldAbstract fld;
        String label_id;
        Class flc;


        // создаём объект с описанием каждого атрибута
        for (String uid : cpa.getPropertyNames()) {
            PropertyAdapter pad = cpa.getPropertyAdapter(uid);

            // игнорируем свойство, если его нельзя читать
            if (!pad.isRead()) {
                continue;
            }

            // игнорируем свойство, если оно помечено аннотацией NonVisual
            if (pad.getAnnotation(NonVisual.class) != null) {
                continue;
            }

            // читаем название типа атрибута
            attributetype = pad.getType();
            // System.out.println(attributetype.getName());


            // чтобы в метаданных атрибута наверняка ничего не было

            // класс с метаданными
            flc = null;

            // экземпляр класса с метаданными
            fld = null;

            // проверяем, зарегистрировано ли имя атрибута в конфигурации фабрики
            // System.out.println("Searching configuration for "+forClass.getName() + "." + uid);
            if (configuration.containsKey(forClass.getName() + "." + uid)) {
                flc = configuration.get(attributetype.getName());
            }


            // если атрибут не зарегистрирован по имени,
            // проверяем, зарегистрирован ли тип атрибута в конфигурации фабрики
            if (flc == null && configuration.containsKey(attributetype.getName())) {
                flc = configuration.get(attributetype.getName());
            }

            // если запись в конфигурации найдена
            if (flc != null) {
                try {
                    // Создаём объект с информацией о колонке в таблице,
                    // используется конструктор без параметров
                    fld = (GridFieldAbstract) flc.newInstance();

                    // устанавливаем атрибуты:
                    // UID
                    fld.setUid(uid);

                    // attributeName
                    fld.setAttributeName(uid);

                    // метка поля, заголовок колонки
                    label_id = forClass.getName() + "." + uid;
                    fld.setLabel(label_id);

                    // объект для хранения информации о видимости колонки
                    fld.setFieldView(new GridFieldView()._setUid(uid)._setLabel(fld.getLabel()));

                    // объект для хранения информации об упорядочении строк
                    fld.setFieldSort(new GridFieldSort()._setUid(uid)._setSortType(GridFieldSortType.NONE)._setAttributeName(uid)._setLabel(fld.getLabel()));

                    // объект с условиями фильтрации
                    fld._setFilterElementList(fld.createFilterElementList());
                } catch (InstantiationException ex) {
                    log.error(ex.getMessage());
                } catch (IllegalAccessException ex) {
                    log.error(ex.getMessage());
                }
                // если экземпляр создать не удалось
                if (fld == null) {
                    fld = new GridFieldCalculable(uid);
                }
                // добавляем поле в модель
                fields.add(fld);
            }


        }

        return fields;
    }
}
