package orion.cpu.dao;

import br.com.arsmachina.dao.DAO;
import java.util.List;
import orion.cpu.baseentities.NamedEntity;

/**
 * Базовый интерфейс DAO для NamedEntity
 * @param <T> тип сущности
 * @author sl
 */
public interface NamedEntityDAO<T extends NamedEntity<?>> extends DAO<T, Integer> {

    /**
     * Найти по имени
     * @param name имя
     * @return список сущностей
     */
    public List<T> findByName(String name);
}
