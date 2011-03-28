package orion.cpu.services;

/**
 * Интерфейс сервиса хранимых в базе данных констант.
 * @author sl
 */
//TODO В статье http://tapestry.apache.org/tapestry5.1/tapestry-ioc/provider.html
//описано как добавлять константы через Inject. Нужно через contributeMasterObjectProvider
//зарегистировать New Providers
public interface StoredConstantsSource {

    /**
     * Получить константу.
     * @param <T> тип константы
     * @param clasz класс константы
     * @param key ключ
     * @return константа
     */
    <T> T get(Class<T> clasz, String key);

    /**
     * Проверть есть ли константа.
     * @param clasz класс константы
     * @param key ключ
     * @return true если константа хранится в базе, иначе false
     */
    boolean contains(Class<?> clasz, String key);

    /**
     * Сохранить константу.
     * @param <T> тип константы
     * @param clasz класс константы
     * @param key ключ
     * @param value значение
     * @return true если значение было сохранено
     */
    <T> boolean put(Class<T> clasz, String key, T value);

    /**
     * Удалить константу.
     * @param clasz класс константы
     * @param key ключ
     */
    void remove(Class<?> clasz, String key);
}
