package orion.cpu.controllers;

import java.util.List;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Контроллер для справочников
 * @param <T> справочник
 * @author sl
 */
public interface ReferenceController<T extends ReferenceEntity<?>> extends NamedEntityController<T> {

    /**
     * Найти по символическому ключу
     * @param key ключ
     * @return список сущностей
     */
    public T findByKey(String key);

    /**
     * Найти все записи кроме образца не являющиеся псевдонимами
     * @param example образец
     * @return список сущностей
     */
    public List<T> findByAliasToIsNullAndExceptExample(T example);

    /**
     * Найти все записи кроме образца не являющиеся псевдонимами и не устаревшие
     * @param example образец
     * @return список сущностей
     */
    public List<T> findByAliasToIsNullAndIsNotObsoleteAndExceptExample(T example);
}
