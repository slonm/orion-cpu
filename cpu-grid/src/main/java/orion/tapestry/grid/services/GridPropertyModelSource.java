package orion.tapestry.grid.services;

import java.beans.IntrospectionException;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * Класс конструирует список полей по классу-сущности
 * @author Gennadiy Dobrovolsky
 */
public interface GridPropertyModelSource {

    /**
     * Основной метод генерации одного поля для модели данных
     *
     * @param forClass класс сущности, который описывает атрибуты
     * @return объект, который описывает колонки в таблице
     * @throws IntrospectionException возникает только если не удалось получить информацию из класса forClass
     */
    public GridPropertyModelInterface getGridPropertyModel(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages);
}
