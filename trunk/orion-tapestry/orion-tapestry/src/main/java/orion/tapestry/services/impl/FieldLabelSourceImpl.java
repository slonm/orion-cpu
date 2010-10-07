package orion.tapestry.services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.internal.services.impl.GlobalMessages;
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
    private final GlobalMessages gMessages;

    public FieldLabelSourceImpl(GlobalMessages messages) {
        this.gMessages = messages;
    }

    @Override
    public String get(Class<?> bean, String propertyName, Locale locale) {
        return get(bean, propertyName, null, locale);
    }

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
        return get(bean, propertyName, messages, null);
    }

    private String get(final Class<?> bean, String propertyName, Messages messages, Locale locale) {
        Defense.notNull(bean, "bean");
        Defense.notBlank(propertyName, "propertyName");
        LOG.debug("processing '{}' property", propertyName);
        Class<?> pView = bean;
        while (pView != null) {
            String fullName = PREFIX + pView.getName() + "." + propertyName;
            if (contains(fullName, messages, locale)) {
                LOG.debug("get property by full name value: {}", fullName);
                return get(fullName, messages, locale);
            }
            pView = pView.getSuperclass();
        }
        if (contains(propertyName, messages, locale)) {
            LOG.debug("get property by as-is name value: {}", propertyName);
            return get(propertyName, messages, locale);
        }
        String typeName = PREFIX + new PropertyClassName(bean).get(propertyName);
        if (typeName != null && contains(typeName, messages, locale)) {
            LOG.debug("get property by type name value: {}", typeName);
            return get(typeName, messages, locale);
        }
        return null;
    }

    private String get(String property, Messages messages, Locale locale) {
        if (messages == null) {
            return gMessages.get(property, locale);
        } else {
            return messages.get(property);
        }
    }

    private boolean contains(String property, Messages messages, Locale locale) {
        if (messages == null) {
            return gMessages.contains(property, locale);
        } else {
            return messages.contains(property);
        }
    }
}
