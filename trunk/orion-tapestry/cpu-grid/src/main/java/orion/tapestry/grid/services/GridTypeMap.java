package orion.tapestry.grid.services;

import java.util.Map;
import orion.tapestry.grid.lib.field.GridFieldAbstract;

/**
 * Класс, который устанавливает соответствие между типом поля
 * и свойствами колонки в таблице
 * @author Genadoiy Dobrovolsky
 */
public interface GridTypeMap {
    /**
     * Метод, который отображает тип атрибута в тип колонки в таблице
     * @param attribyteType
     * @return 
     */
    Class getField(String attribyteType);

    /**
     * Метод, который возвращает текущую конфигурацию
     */
    Map<String, Class> getConfiguration();
}
