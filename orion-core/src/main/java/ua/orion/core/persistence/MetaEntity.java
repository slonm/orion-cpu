package ua.orion.core.persistence;

import java.util.Locale;
import org.apache.tapestry5.ioc.Messages;

/**
 * Описатель сущности. Дополнение к JPA Metamodel
 * @author sl
 */
public interface MetaEntity {
    
    Class<?> getEntityClass();
    
    String getEntityName();
    
    /**
     * @return 
     * @throws IllegalArgumentException if type not entity
     */
    boolean supportUKey();

    boolean supportUserPresentable();
    
    /**
     * 
     * @return null if not supported
     */
    String getUserPresentableAttributeName();
    
    /**
     * 
     * @return null if not supported
     */
    String getUKeyAttributeName();
    
    /**
     * Возвращает ключ в формате "entity." + type.getSimpleName()
     * @return label
     */
    String getLabel(Messages messages);

    /**
     * Возвращает ключ в формате "entity." + type.getSimpleName()
     * @return label
     */
    String getLabel();

    /**
     * Возвращает ключ в формате "property." + type.getSimpleName() + "." + propertyName
     * @return label
     */
    String getPropertyLabel(String propertyName, Messages messages);

    /**
     * Возвращает ключ в формате "property." + type.getSimpleName() + "." + propertyName
     * @return label
     */
    String getPropertyLabel(String propertyName);
}
