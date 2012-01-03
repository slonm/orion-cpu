/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.services;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.orion.core.utils.Defense;

/**
 *
 * @author slobodyanuk
 * TODO протестить
 * TODO брать подписи свойств для интерфейсов
 */
public class ModelLabelSourceImpl implements ModelLabelSource {

    private static final Logger LOG = LoggerFactory.getLogger(ModelLabelSourceImpl.class);
    private final PropertyAccess propertyAccess;

    public ModelLabelSourceImpl(PropertyAccess propertyAccess) {
        this.propertyAccess = propertyAccess;
    }

    @Override
    public String getEntityLabel(Class<?> bean, Messages messages) {
        Defense.notNull(bean, "bean");
        Defense.notNull(messages, "messages");
        Class<?> pView = bean;
        while (pView != null) {
            String fullName = "entity." + pView.getSimpleName();
            if (messages.contains(fullName)) {
                LOG.debug("get label by key: {}", fullName);
                return messages.get(fullName);
            }
            pView = pView.getSuperclass();
        }
        if (messages.contains(bean.getSimpleName())) {
            LOG.debug("get label by as-is name value: {}", bean.getSimpleName());
            return messages.get(bean.getSimpleName());
        }
        return null;
    }

    @Override
    public String getPropertyLabel(Class<?> bean, String propertyName, Messages messages) {
        Defense.notNull(bean, "bean");
        Defense.notBlank(propertyName, "propertyName");
        Defense.notNull(messages, "messages");
        Class<?> pView = bean;
        while (pView != null) {
            String fullName = "property." + pView.getSimpleName() + "." + propertyName;
            if (messages.contains(fullName)) {
                LOG.debug("get label by key: {}", fullName);
                return messages.get(fullName);
            }
            pView = pView.getSuperclass();
        }
        if (messages.contains(bean.getSimpleName())) {
            LOG.debug("get label by as-is name value: {}", propertyName);
            return messages.get(propertyName);
        }
        return getEntityLabel(propertyAccess.getAdapter(bean).getPropertyAdapter(propertyName).getType(), messages);
    }

    @Override
    public String getCellPropertyLabel(Class<?> bean, String propertyName, Messages messages) {
        Defense.notNull(bean, "bean");
        Defense.notBlank(propertyName, "propertyName");
        Defense.notNull(messages, "messages");
        Class<?> pView = bean;
        while (pView != null) {
            String fullName = "property-cell." + pView.getSimpleName() + "." + propertyName;
            if (messages.contains(fullName)) {
                LOG.debug("get label by key: {}", fullName);
                return messages.get(fullName);
            }
            pView = pView.getSuperclass();
        }
        pView = bean;
        while (pView != null) {
            String fullName = "property." + pView.getSimpleName() + "." + propertyName;
            if (messages.contains(fullName)) {
                LOG.debug("get label by key: {}", fullName);
                return messages.get(fullName);
            }
            pView = pView.getSuperclass();
        }
        if (messages.contains(bean.getSimpleName())) {
            LOG.debug("get label by as-is name value: {}", propertyName + "-cell");
            return messages.get(propertyName + "-cell");
        }
        if (messages.contains(bean.getSimpleName())) {
            LOG.debug("get label by as-is name value: {}", propertyName);
            return messages.get(propertyName);
        }
        return getEntityLabel(propertyAccess.getAdapter(bean).getPropertyAdapter(propertyName).getType(), messages);
    }
}
