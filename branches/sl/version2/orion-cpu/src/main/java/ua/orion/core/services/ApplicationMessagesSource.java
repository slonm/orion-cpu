package ua.orion.core.services;

import java.util.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.UsesConfiguration;
import org.apache.tapestry5.services.UpdateListener;

/**
 * Каталог сообщений уровня приложения
 * Каждый ресурс с сообщениями должен предоставлять свои ключи.
 * В случае пересечения имен ключей в разных ресурсах результат непредсказуем
 * @author sl
 */
@UsesConfiguration(String.class)
public interface ApplicationMessagesSource{

    /**
     * Return localized messages.
     */
    Messages getMessages(Locale locale);
    
    /**
     * Discards all stored messages and clear Resource bundle cache.
     */
    void clearCache();
}
