package ua.mihailslobodyanuk.utils.reflect.generics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import ua.mihailslobodyanuk.utils.BeanUtils;
import ua.mihailslobodyanuk.utils.StringUtils;

/**
 *
 * @author sl
 */
//TODO Протестировать
public abstract class PropertyInfo<T> {

    private static final Logger LOG = Logger.getLogger(PropertyInfo.class.getName());
    private final Class clazz;
    private final Object obj;

    protected PropertyInfo(Object obj) {
        this.obj = obj;
        this.clazz = obj.getClass();
    }

    protected PropertyInfo(Class clazz) {
        this.obj = null;
        this.clazz = clazz;
    }

    protected abstract T fieldInfo(Field field, Object obj) throws Exception;

    protected abstract T methodInfo(Method method, Object obj) throws Exception;

    public T get(String propertyName) {
        Class curClass = clazz;
        while (curClass != null) {
            LOG.fine("Search info for " + propertyName + " in " + curClass.getName());
            try {
                LOG.fine("try to get info from field " + propertyName);
                Field f = curClass.getDeclaredField(propertyName);
                return fieldInfo(f, obj);
            } catch (Exception ex) {
                String capitalizedPropertyName = StringUtils.capitalize(BeanUtils.stripMemberName(propertyName));
                LOG.fine("Failed.\ntry to get info from method get" + capitalizedPropertyName + "()");
                try {
                    Method m = curClass.getDeclaredMethod("get" + capitalizedPropertyName);
                    return methodInfo(m, obj);
                } catch (Exception ex1) {
                    LOG.fine("Failed.\ntry to get info from method is" + capitalizedPropertyName + "()");
                    try {
                        Method m = curClass.getClass().getDeclaredMethod("is" + capitalizedPropertyName);
                        return methodInfo(m, obj);
                    } catch (Exception ex2) {
                    }
                }
            }
            curClass = curClass.getSuperclass();
        }
        LOG.fine("Filed.\nreturn null");
        return null;
    }
}
