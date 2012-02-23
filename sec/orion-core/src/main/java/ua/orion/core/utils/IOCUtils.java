/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.utils;

import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.spi.PersistenceUnitInfo;
import org.apache.tapestry5.jpa.EntityManagerSource;

/**
 *
 * @author sl
 */
public class IOCUtils {

    /**
     * Используется в адвайзерах для поиска нужного метода
     */
    public static Method getMethod(Class<?> type, String methodName, Class<?>... methodArguments) {
        try {
            return type.getMethod(methodName, methodArguments);
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String getDefaultPersistenceUnitName(EntityManagerSource entityManagerSource)
    {
        List<PersistenceUnitInfo> entityManagers = entityManagerSource.getPersistenceUnitInfos();

        if (entityManagers.size() == 1)
            return entityManagers.get(0).getPersistenceUnitName();

        throw new RuntimeException("Unable to locate a single PersistenceUnitInfo. ");
    }
    
}
