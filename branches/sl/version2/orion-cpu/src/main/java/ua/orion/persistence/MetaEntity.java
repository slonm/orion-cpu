package ua.orion.persistence;

import java.util.Locale;
import org.apache.tapestry5.ioc.Messages;

/**
 * Описатель сущности. Доплнение к JPA Metamodel
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
     * из ApplicationMessagesSource
     * @return label
     */
    String getLabel(Locale locale);

    /**
     * Возвращает ключ в формате "property." + type.getSimpleName() + "." + propertyName
     * @return label
     */
    String getPropertyLabel(String propertyName, Messages messages);

    /**
     * Возвращает ключ в формате "property." + type.getSimpleName() + "." + propertyName
     * из ApplicationMessagesSource
     * @return label
     */
    String getPropertyLabel(String propertyName, Locale locale);
}
