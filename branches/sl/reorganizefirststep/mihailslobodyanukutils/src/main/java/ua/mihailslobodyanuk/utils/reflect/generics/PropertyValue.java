package ua.mihailslobodyanuk.utils.reflect.generics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author sl
 */
//TODO Протестировать
public class PropertyValue extends PropertyInfo<Object>{

    protected PropertyValue(Object obj) {
        super(obj);
    }

    protected PropertyValue(Class clazz) {
        super(clazz);
    }

    @Override
    protected Object fieldInfo(Field field, Object obj) throws Exception {
        return field.get(obj);
    }

    @Override
    protected Object methodInfo(Method method, Object obj) throws Exception {
        return method.invoke(obj);
    }

    public static Object get(Class clazz, String propertyName){
        return new PropertyValue(clazz).get(propertyName);
    }

    public static Object get(Object obj, String propertyName){
        return new PropertyValue(obj).get(propertyName);
    }
}
