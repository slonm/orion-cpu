/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.grid.services;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.ioc.internal.services.ClassPropertyAdapterImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.impl.GridFieldCalculable;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.sort.GridFieldSortType;
import orion.tapestry.grid.lib.field.view.GridFieldView;

/**
 *
 * @author dobro
 */
public class GridFieldFactoryImpl implements GridFieldFactory {

    private Map<String, Class> configuration;
    private final Logger log;
    private final PropertyAccess propertyAccess;

    /**
     * @param configuration конфигурация, содержащая отображение тип данных=> тип колонки
     */
    public GridFieldFactoryImpl(Map<String, Class> _configuration, Logger log, PropertyAccess propertyAccess) {
        this.configuration = _configuration;
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
     * @throws IntrospectionException возникает только если не удалось получить информацию из класса forClass
     */
    @Override
    public List<GridFieldAbstract> getFields(Class forClass) throws IntrospectionException {

        // этот список будем возвращать из метода
        ArrayList<GridFieldAbstract> fields = new ArrayList<GridFieldAbstract>();


        //
        ClassPropertyAdapter cpa = this.propertyAccess.getAdapter(forClass);


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

//    public ClassPropertyAdapter getClassPropertyAdapter(Class forClass) throws IntrospectionException {
//        BeanInfo info = Introspector.getBeanInfo(forClass);
//        List<PropertyDescriptor> descriptors = CollectionFactory.newList();
//        addAll(descriptors, info.getPropertyDescriptors());
//
//        if (forClass.isInterface()) {
//            addPropertiesFromExtendedInterfaces(forClass, descriptors);
//        }
//        ClassPropertyAdapter cpa = propertyAccess.getAdapter(forClass);
//        return cpa;
//    }
//
//    private <T> void addAll(List<T> list, T[] array) {
//        list.addAll(Arrays.asList(array));
//    }
//
//    private void addPropertiesFromExtendedInterfaces(Class forClass, List<PropertyDescriptor> descriptors) throws IntrospectionException {
//        LinkedList<Class> queue = CollectionFactory.newLinkedList();
//
//        // Seed the queue
//        addAll(queue, forClass.getInterfaces());
//
//        while (!queue.isEmpty()) {
//            Class c = queue.removeFirst();
//
//            BeanInfo info = Introspector.getBeanInfo(c);
//
//            addAll(descriptors, info.getPropertyDescriptors());
//            addAll(queue, c.getInterfaces());
//        }
//    }
}
