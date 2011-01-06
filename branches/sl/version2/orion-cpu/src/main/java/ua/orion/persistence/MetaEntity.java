package ua.orion.persistence;

import java.util.Locale;
import org.apache.tapestry5.ioc.Messages;

/**
 *
 * @author sl
 */
public interface MetaEntity {
    
    /**
     * 
     * @param type
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
    
    String getLabel(Messages messages);

    String getLabel(Locale locale);

    String getPropertyLabel(String propertyName, Messages messages);

    String getPropertyLabel(String propertyName, Locale locale);
}
