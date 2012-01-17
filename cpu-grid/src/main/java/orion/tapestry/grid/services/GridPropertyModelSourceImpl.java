package orion.tapestry.grid.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.ioc.Messages;
import org.slf4j.Logger;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelAdapter;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 *
 * @author dobro
 */
public class GridPropertyModelSourceImpl implements GridPropertyModelSource {

    private Map<String, Class> configuration;
    private final Logger log;

    /**
     * @param configuration конфигурация, содержащая отображение тип данных=> тип колонки
     */
    public GridPropertyModelSourceImpl(Map<String, Class> _configuration, Logger log) {
        this.configuration = _configuration;
        this.log = log;
    }

    @Override
    public GridPropertyModelInterface getGridPropertyModel(
            GridBeanModel _gridBeanModel,
            String _propertyName,
            PropertyConduit _propertyConduit,
            Messages messages) {

        Class<GridPropertyModelInterface> flc = null;


        // проверяем, зарегистрировано ли имя атрибута в конфигурации фабрики
        String attributeFullName = _gridBeanModel.getBeanType().getName() + "." + _propertyName;
        if (configuration.containsKey(attributeFullName)) {
            flc = (Class<GridPropertyModelInterface>) configuration.get(attributeFullName);
            log.info(String.format("Model for attribute named %s is found", attributeFullName));
        }



        // если атрибут не зарегистрирован по имени,
        // проверяем, зарегистрирован ли тип атрибута в конфигурации фабрики
        if (_propertyConduit != null) {
            String attributeTypeName = _propertyConduit.getPropertyType().getName();
            if (flc == null && configuration.containsKey(attributeTypeName)) {
                flc = (Class<GridPropertyModelInterface>) configuration.get(attributeTypeName);
                log.info(String.format("Model for type %s of attribute %s is found", attributeTypeName, attributeFullName));
            }
        }


        GridPropertyModelInterface newPropertyModel = null;
        // если запись в конфигурации не найдена, то возвращаем обобщённую модель
        if (flc == null) {
            newPropertyModel= new GridPropertyModelAdapter(_gridBeanModel, _propertyName, _propertyConduit, messages).sortable(false);
            return newPropertyModel;
        }

        
        try {
            log.info(String.format("Instantiating model for %s as %s object", attributeFullName, flc.getClass().getName()));
            Constructor<GridPropertyModelInterface> constructor;
            constructor = flc.getConstructor(GridBeanModel.class, String.class, PropertyConduit.class, Messages.class);
            newPropertyModel = constructor.newInstance(_gridBeanModel, _propertyName, _propertyConduit, messages);
        } catch (InstantiationException ex) {
            log.info(ex.getLocalizedMessage());
        } catch (IllegalAccessException ex) {
            log.info(ex.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            log.info(ex.getLocalizedMessage());
        } catch (InvocationTargetException ex) {
            log.info(ex.getLocalizedMessage());
        } catch (NoSuchMethodException ex) {
            log.info(ex.getLocalizedMessage());
        } catch (SecurityException ex) {
            log.info(ex.getLocalizedMessage());
        }
        // use default model without filters
        if (newPropertyModel == null) {
            newPropertyModel = new GridPropertyModelAdapter(_gridBeanModel, _propertyName, _propertyConduit, messages);
        }
        return newPropertyModel;
    }
}
