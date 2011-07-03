package ua.orion.core.services;

import java.util.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.UsesConfiguration;

/**
 * Каталог строковых ресурсов приложения. Является мостом между
 * ResourceBundle и org.apache.tapestry5.ioc.Messages. В конфигурации
 * получает список имен ResourceBundle и строит комплексный bundle.
 * Конфигурация по умолчанию состоит из ресурсов одноименных с именами всех
 * ModelLibrary расположенных в пакетах по умолчанию.
 * Каждый ResourceBundle должен предоставлять свои ключи.
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
     * Return localized messages for thread locale.
     */
    Messages getMessages();
    
    /**
     * Discards all stored messages and clear Resource bundle cache.
     */
    void clearCache();
}
