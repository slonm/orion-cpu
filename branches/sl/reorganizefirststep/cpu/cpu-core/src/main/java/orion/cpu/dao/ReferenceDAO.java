package orion.cpu.dao;

import java.util.List;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Базовый интерфейс DAO для справочников
 * @param <T> тип стравочника
 * @author sl
 */
public interface ReferenceDAO<T extends ReferenceEntity<?>> extends NamedEntityDAO<T> {

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
