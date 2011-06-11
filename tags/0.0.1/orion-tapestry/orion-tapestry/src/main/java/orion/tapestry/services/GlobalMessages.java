package orion.tapestry.services;

import java.util.*;

/**
 * Глобальный каталог сообщений и
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
public interface GlobalMessages {
     /**
     * Returns true if the bundle contains the named key.
     */
    boolean contains(String key, Locale locale);

    /**
     * Returns the localized message for the given key. If catalog does not contain such a key, then a modified version
     * of the key is returned (converted to upper case and enclosed in brackets).
     *
     * @param key
     * @return localized message for key, or placeholder
     */
    public String get(String key, Locale locale);

 }
