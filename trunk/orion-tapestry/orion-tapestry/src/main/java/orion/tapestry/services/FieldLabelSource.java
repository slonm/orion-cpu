package orion.tapestry.services;

import org.apache.tapestry5.ioc.Messages;

/**
 * Интерфейс источника подписей для полей бина. Сервис поддерживает аннотацию
 * {@link orion.tapestry.beaneditor.FieldLabel}
 * @author sl
 */
public interface FieldLabelSource {

    /**
     * Возвращает подпись поля бина
     * @param bean класс бина
     * @param propertyName имя свойства бина. Если CLASS_NAME_KEY, то вернет подпись самого бина
     * @param messages текущий каталог сообщений. Если null, то игнорируется
     * @return подпись поля бина. null если подпись не найдена
     */
    String get(Class<?> bean, String propertyName, Messages messages);
}
