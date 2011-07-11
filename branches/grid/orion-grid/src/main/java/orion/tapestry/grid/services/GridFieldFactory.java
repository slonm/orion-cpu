package orion.tapestry.grid.services;

import java.util.*;
import orion.tapestry.grid.lib.field.GridFieldAbstract;

/**
 * Класс конструирует список полей по классу-сущности
 * @author Gennadiy Dobrovolsky 
 */
public interface GridFieldFactory {

    /**
     * Основной метод для генерации списка полей для модели данных
     *
     * @param forClass класс сущности, который описывает атрибуты
     * @param configuration конфигурация, содержащая отображение тип данных=> тип колонки
     * @param messages текстовые сообщения для интерфейса, использует стандартный механизм локализации Tapestry
     * @return список полей, которые описывают колонки в таблице
     */
    List<GridFieldAbstract> getFields(
            Class forClass,
            Map<String, Class> configuration);
}
