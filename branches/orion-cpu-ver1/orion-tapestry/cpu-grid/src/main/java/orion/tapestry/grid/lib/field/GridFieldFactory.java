package orion.tapestry.grid.lib.field;

import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.field.sort.GridFieldSortType;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tapestry5.ioc.internal.services.ClassPropertyAdapterImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import orion.tapestry.grid.lib.field.impl.*;
import org.apache.tapestry5.beaneditor.NonVisual;

/**
 * Класс конструирует список полей по классу-сущности
 * @author Gennadiy Dobrovolsky 
 */
public class GridFieldFactory {

    /**
     * Основной метод для генерации списка полей для модели данных
     *
     * @param forClass класс сущности, который описывает атрибуты
     * @param configuration конфигурация, содержащая отображение тип данных=> тип колонки
     * @param messages текстовые сообщения для интерфейса, использует стандартный механизм локализации Tapestry
     * @return список полей, которые описывают колонки в таблице
     * @throws IntrospectionException возникает только если не удалось получить информацию из класса forClass
     */
    public static List<GridFieldAbstract> getFields(
            Class forClass,
            Map<String, Class> configuration) throws IntrospectionException {

        // этот список будем возвращать из метода
        ArrayList<GridFieldAbstract> fields = new ArrayList<GridFieldAbstract>();

        ClassPropertyAdapter cpa = getClassPropertyAdapter(forClass);


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
                    Logger.getLogger(GridFieldFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(GridFieldFactory.class.getName()).log(Level.SEVERE, null, ex);
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

    public static ClassPropertyAdapter getClassPropertyAdapter(Class forClass) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(forClass);
        List<PropertyDescriptor> descriptors = CollectionFactory.newList();
        addAll(descriptors, info.getPropertyDescriptors());

        if (forClass.isInterface()) {
            addPropertiesFromExtendedInterfaces(forClass, descriptors);
        }
        ClassPropertyAdapter cpa = new ClassPropertyAdapterImpl(forClass, descriptors);
        return cpa;
    }

//    public static List<GridFieldAbstract> getFields(Class forClass, Map<String, Class> configuration) throws IntrospectionException {
//        return getFields(forClass, configuration);
//    }
    private static <T> void addAll(List<T> list, T[] array) {
        list.addAll(Arrays.asList(array));
    }

    private static void addPropertiesFromExtendedInterfaces(Class forClass, List<PropertyDescriptor> descriptors) throws IntrospectionException {
        LinkedList<Class> queue = CollectionFactory.newLinkedList();

        // Seed the queue
        addAll(queue, forClass.getInterfaces());

        while (!queue.isEmpty()) {
            Class c = queue.removeFirst();

            BeanInfo info = Introspector.getBeanInfo(c);

            addAll(descriptors, info.getPropertyDescriptors());
            addAll(queue, c.getInterfaces());
        }
    }
//    public static void main(String[] args) {
//        Map<String, Class> configuration = new TreeMap<String, Class>();
//        configuration.put("java.lang.String", GridFieldString.class);
//
//        configuration.put("byte", GridFieldInteger.class);
//        configuration.put("java.lang.Byte", GridFieldInteger.class);
//
//        configuration.put("int", GridFieldInteger.class);
//        configuration.put("java.lang.Integer", GridFieldInteger.class);
//
//        configuration.put("long", GridFieldInteger.class);
//        configuration.put("java.lang.Long", GridFieldInteger.class);
//
//        configuration.put("float", GridFieldFloat.class);
//        configuration.put("java.lang.Float", GridFieldFloat.class);
//
//        configuration.put("double", GridFieldFloat.class);
//        configuration.put("java.lang.Double", GridFieldFloat.class);
//
//        configuration.put("java.util.Date", GridFieldDate.class);
//        try {
//            List<GridField> flds = GridFieldFactory.getFields(test.grid.lib.contact.class, configuration);
//            System.out.println(flds.size());
//        } catch (IntrospectionException ex) {
//            Logger.getLogger(GridFieldFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
}
