package ua.orion.core.services;

import org.apache.tapestry5.ioc.Messages;

/**
 *
 * @author slobodyanuk
 */
public interface ModelLabelSource {

    /**
     * Подписи сущностей
     * Ищет подпись в каталоге сообщений в следующей последовательности:
     * 1. запись с именем в формате entity.EntityName
     * 2. запись с именем в формате entity.BaseEntityName, где BaseEntityName имя предка бина
     * 3. запись с именем в формате EntityName
     */
    String getEntityLabel(Class<?> bean, Messages messages);
    String getEntityLabel(Class<?> bean);

    /**
     * Подписи свойств
     * Ищет подпись в каталоге сообщений в следующей последовательности:
     * 1. запись с именем в формате property.EntityName.propName
     * 2. запись с именем в формате property.BaseEntityName.propName, где BaseEntityName имя предка бина
     * 3. запись с именем в формате propName
     * 4. запись для типа данных свойства
     */
    String getPropertyLabel(Class<?> bean, String propertyName, Messages messages);
    String getPropertyLabel(Class<?> bean, String propertyName);

    /**
     * Подписи свойств в каталоге сообщений в заголовке столбцов
     * Ищет подпись в следующей последовательности:
     * 1. запись с именем в формате property-cell.EntityName.propName
     * 2. запись с именем в формате property-cell.BaseEntityName.propName, где BaseEntityName имя предка бина
     * 3. запись с именем в формате property.EntityName.propName
     * 4. запись с именем в формате property.BaseEntityName.propName, где BaseEntityName имя предка бина
     * 5. запись с именем в формате propName-cell
     * 6. запись с именем в формате propName
     * 7. запись для типа данных свойства
     */
    String getCellPropertyLabel(Class<?> bean, String propertyName, Messages messages);
    String getCellPropertyLabel(Class<?> bean, String propertyName);
}
