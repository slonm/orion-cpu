package orion.cpu.controllers;

import br.com.arsmachina.controller.Controller;
import java.util.List;
import orion.cpu.baseentities.NamedEntity;

/**
 * Контроллер для {@see NamedEntity}
 * @param <T> сущность
 * @author sl
 */
public interface NamedEntityController<T extends NamedEntity<?>> extends Controller<T, Integer> {

    /**
     * Найти по имени
     * @param name имя
     * @return список сущностей
     */
    public List<T> findByName(String name);

    /**
     * Найти по имени первую запись
     * @param name имя
     * @return список сущностей
     */
    public T findByNameFirst(String name);
}
