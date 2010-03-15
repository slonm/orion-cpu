package orion.tapestry.services.impl;

import java.util.*;
import org.apache.tapestry5.internal.services.ComponentMessagesSource;
import org.apache.tapestry5.internal.services.MessagesBundle;
import org.apache.tapestry5.internal.services.MessagesSource;
import org.apache.tapestry5.internal.services.MessagesSourceImpl;
import org.apache.tapestry5.internal.util.URLChangeTracker;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.apache.tapestry5.services.AssetSource;

/**
 * Консультант {@link ComponentMessagesSource}
 * Сервис получает вклад в виде упорядоченного списка имен ресурсов (файлов .properties).
 * При вызове метода ComponentMessagesSource.getMessages делает постобработку возвращаемого
 * каталога сообщений. Теперь сообщения ищутся в следующей последовательности:
 * 1. Собственные сообщения компонента
 * 2. Сообщения компонента-контейнера
 * 3. Сообщения из ресурсов согласно списка
 * Внимание! Сервис использует internal ресурсы tapestry
 * @author sl
 */
public class GlobalMessageAppender implements MethodAdvice {
    //Нелокализованные ресурсы

    private final List<Resource> resources = new LinkedList<Resource>();
    //Локализованные сообщения
    private final Map<Locale, List<Messages>> locResources = new HashMap<Locale, List<Messages>>();
    private final MessagesSource messagesSource;

    private static class InternalBundle implements MessagesBundle {

        private final Resource resource;

        public InternalBundle(Resource resource) {
            this.resource = resource;
        }

        @Override
        public Resource getBaseResource() {
            return resource;
        }

        @Override
        public Object getId() {
            return resource;
        }

        @Override
        public MessagesBundle getParent() {
            return null;
        }
    }

    private class InternalMessages implements Messages {

        private final Messages messages;
        private final Locale locale;

        private InternalMessages(Messages messages, Locale locale) {
            this.messages = messages;
            this.locale = locale;
        }

        @Override
        public boolean contains(String key) {
            if (messages.contains(key)) {
                return true;
            } else {
                for (Messages m : getMessagesList(locale)) {
                    if (m.contains(key)) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override
        public String get(String key) {
            if (messages.contains(key)) {
                return messages.get(key);
            } else {
                for (Messages m : getMessagesList(locale)) {
                    if (m.contains(key)) {
                        return m.get(key);
                    }
                }
                return messages.get(key);
            }
        }

        @Override
        public MessageFormatter getFormatter(String key) {
            return messages.getFormatter(key);
        }

        @Override
        public String format(String key, Object... args) {
            return messages.format(key, args);
        }
    }

    public GlobalMessageAppender(List<String> configuration,
            ClasspathURLConverter classpathURLConverter,
            final AssetSource assetSource) {
        for (String name : configuration) {
            resources.add(assetSource.resourceForPath(name));
        }
        messagesSource = new MessagesSourceImpl(new URLChangeTracker(classpathURLConverter));
    }

    private List<Messages> getMessagesList(Locale locale) {
        if (!locResources.containsKey(locale)) {
            List<Messages> list = new LinkedList<Messages>();
            for (Resource r : resources) {
                list.add(messagesSource.getMessages(new InternalBundle(r), locale));
            }
            locResources.put(locale, list);
        }
        return locResources.get(locale);
    }

    @Override
    public void advise(Invocation invocation) {
        invocation.proceed();
        if (invocation.getMethodName().equals("getMessages")) {
            invocation.overrideResult(new InternalMessages((Messages) invocation.getResult(),
                    (Locale) invocation.getParameter(1)));
        }
    }
}
