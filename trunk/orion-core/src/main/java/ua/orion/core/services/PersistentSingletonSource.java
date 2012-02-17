package ua.orion.core.services;

import java.io.Serializable;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;

/**
 * Интерфейс сервиса хранимых переменных.
 * В конфигурации можно задавать сущности, в которых хранятся переменные простых
 * сериализуемых типов key-тип переменной, value - сущность
 * @author sl
 */
@UsesMappedConfiguration(key = Class.class, value = Class.class)
public interface PersistentSingletonSource {

    /**
     * Получить константу.
     * @param <T> тип константы
     * @param clasz класс константы
     * @param key ключ
     * @return константа
     */
    <T extends Serializable> T get(Class<T> clasz, String key);

    /**
     * Проверить есть ли константа.
     * @param clasz класс константы
     * @param key ключ
     * @return true если константа хранится в базе, иначе false
     */
    <T extends Serializable> boolean contains(Class<T> clasz, String key);

    /**
     * Сохранить константу.
     * @param clasz класс константы
     * @param key ключ
     * @param value значение
     * @return true если значение было сохранено
     */
    <T extends Serializable> void put(String key, T value);

    /**
     * Удалить константу.
     * @param clasz класс константы
     * @param key ключ
     */
    <T extends Serializable> void remove(Class<T> clasz, String key);
}
