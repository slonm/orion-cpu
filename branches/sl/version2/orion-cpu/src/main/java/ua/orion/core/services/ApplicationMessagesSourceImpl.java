package ua.orion.core.services;

import java.util.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.ioc.util.AbstractMessages;
import ua.orion.core.utils.Defense;

/**
 * @author sl
 * @todo добавить мониторинг изменений файлов ресурсов
 */
public class ApplicationMessagesSourceImpl implements ApplicationMessagesSource {

    //Имена файлов ресурсов
    private final Collection<String> resources = new HashSet<String>();
    //Локализованные каталоги
    private final Map<Locale, Messages> messagesByLocale = new HashMap();
    private final ThreadLocale locale;

    public ApplicationMessagesSourceImpl(Collection<String> configuration,
            ThreadLocale locale) {
        resources.addAll(configuration);
        this.locale=locale;
    }

    @Override
    public Messages getMessages(Locale locale) {
        if (!messagesByLocale.containsKey(locale)) {
            messagesByLocale.put(locale, new InternalMessages(locale));
        }
        return messagesByLocale.get(locale);
    }

    @Override
    public void clearCache() {
        ResourceBundle.clearCache();
        messagesByLocale.clear();
    }

    //TODO Test it
    @Override
    public Messages getMessages() {
        return getMessages(locale.getLocale());
    }

    private class InternalMessages extends AbstractMessages {

        private final Map<String, String> properties = new HashMap();

        private InternalMessages(Locale locale) {
            super(locale);
            for (String name : resources) {
                try {
                    ResourceBundle bundle = ResourceBundle.getBundle(name, locale);
                    Enumeration<String> e = bundle.getKeys();
                    while (e.hasMoreElements()) {
                        String key = e.nextElement();
                        String value = bundle.getString(key);
                        properties.put(key, value);
                    }
                } catch (MissingResourceException ex) {
                }
            }
        }

        @Override
        protected String valueForKey(String key) {
            Defense.notBlank(key, "key");
            return properties.get(key);
        }
    }
}
