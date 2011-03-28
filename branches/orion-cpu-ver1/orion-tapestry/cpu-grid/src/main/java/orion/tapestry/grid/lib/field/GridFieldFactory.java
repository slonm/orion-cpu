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
import org.apache.tapestry5.ioc.Messages;
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
            Map<String, Class> configuration,
            Messages messages) throws IntrospectionException {

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
            if (!pad.isRead()) continue;

            // игнорируем свойство, если оно помечено аннотацией NonVisual
            if (pad.getAnnotation(NonVisual.class) != null) continue;

            // читаем название типа атрибута
            attributetype = pad.getType();
            // System.out.println(attributetype.getName());

            // проверяем, зарегистрирован ли тип атрибута в конфигурации фабрики
            if (configuration.containsKey(attributetype.getName())) {
                flc = configuration.get(attributetype.getName());
                try {
                    // Создаём объект с информацией о колонке в таблице,
                    // используется конструктор без параметров
                    fld = (GridFieldAbstract)flc.newInstance();

                    // устанавливаем атрибуты:
                    // UID
                    fld.setUid(uid);

                    // attributeName
                    fld.setAttributeName(uid);

                    // label - метка поля на текущем языке, заголовок колонки
                    label_id=forClass.getName()+"."+uid + "-label";
                    //label_id=uid + "-label";
                    if(messages !=null){
                        fld.setLabel(messages.get(label_id));
                    }else{
                        fld.setLabel(label_id);
                    }

                    // объект для хранения информации о видимости колонки
                    fld.setFieldView(new GridFieldView()._setUid(uid)._setLabel(fld.getLabel()));

                    // объект для хранения информации об упорядочении строк
                    fld.setFieldSort(new GridFieldSort()._setUid(uid)._setSortType(GridFieldSortType.NONE)._setAttributeName(uid)._setLabel(fld.getLabel()));

                    // объект с условиями фильтрации
                    fld._setFilterElementList(fld.createFilterElementList());

                    // добавляем поле в модель
                    fields.add(fld);
                } catch (InstantiationException ex) {
                    Logger.getLogger(GridFieldFactory.class.getName()).log(Level.SEVERE, null, ex);
                    fld = new GridFieldCalculable(uid);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(GridFieldFactory.class.getName()).log(Level.SEVERE, null, ex);
                    fld = new GridFieldCalculable(uid);
                }
                
            }
        }

        return fields;
    }


    public static ClassPropertyAdapter getClassPropertyAdapter(Class forClass) throws IntrospectionException{
        BeanInfo info = Introspector.getBeanInfo(forClass);
        List<PropertyDescriptor> descriptors = CollectionFactory.newList();
        addAll(descriptors, info.getPropertyDescriptors());

        if (forClass.isInterface()) {
            addPropertiesFromExtendedInterfaces(forClass, descriptors);
        }
        ClassPropertyAdapter cpa = new ClassPropertyAdapterImpl(forClass, descriptors);
        return cpa;
    }


    public static List<GridFieldAbstract> getFields(Class forClass, Map<String, Class> configuration) throws IntrospectionException {
        return getFields(forClass, configuration, null);
    }

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
