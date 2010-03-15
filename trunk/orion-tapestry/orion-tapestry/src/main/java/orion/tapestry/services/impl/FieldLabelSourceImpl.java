package orion.tapestry.services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.tapestry5.ioc.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.services.FieldLabelSource;
import ua.mihailslobodyanuk.utils.Defense;
import ua.mihailslobodyanuk.utils.reflect.generics.PropertyInfo;

/**
 * {@see FieldLabelSource}
 * @author sl
 */
//TODO Использовать внутренний механизм Tapestry для биндинга
//TODO use propertyAccess. Look BeanModelSourceImpl
public class FieldLabelSourceImpl implements FieldLabelSource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldLabelSourceImpl.class);
    private static final String PREFIX = "reflect.";

    private static class PropertyClassName extends PropertyInfo<String> {

        public PropertyClassName(Class<?> clazz) {
            super(clazz);
        }

        @Override
        protected String fieldInfo(Field field, Object obj) throws Exception {
            return field.getType().getName();
        }

        @Override
        protected String methodInfo(Method method, Object obj) throws Exception {
            return method.getReturnType().getName();
        }
    }

    /**
     * {@see FieldLabelSource#get(Class<?> bean, String propertyName, Messages messages)}
     * Ищет подпись в следующей последовательности:
     * 1. В каталоге сообщений запись с именем в формате reflect.BeanName.paramName
     * 2. В каталоге сообщений запись с именем в формате reflect.BaseBeanName.paramName, где BaseBeanName имя предка бина
     * 3. В каталоге сообщений запись с именем в формате paramName
     * 4. В каталоге сообщений запись для типа параметра
     * @author sl
     */
    @Override
    public String get(final Class<?> bean, String propertyName, Messages messages) {
        if (messages == null) {
            return null;
        }
        Defense.notNull(bean, "bean");
        Defense.notBlank(propertyName, "propertyName");
        LOG.debug("processing '{}' property", propertyName);
        Class<?> pView = bean;
        while (pView != null) {
            String fullName = PREFIX + pView.getName() + "." + propertyName;
            if (messages.contains(fullName)) {
                LOG.debug("get property by full name value: {}", fullName);
                return messages.get(fullName);
            }
            pView = pView.getSuperclass();
        }
        if (messages.contains(propertyName)) {
            LOG.debug("get property by as-is name value: {}", propertyName);
            return messages.get(propertyName);
        }
        String typeName = PREFIX + new PropertyClassName(bean).get(propertyName);
        if (typeName != null && messages.contains(typeName)) {
            LOG.debug("get property by type name value: {}", typeName);
            return messages.get(typeName);
        }
        return null;
    }
}
