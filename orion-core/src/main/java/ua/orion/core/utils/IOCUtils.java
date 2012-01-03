/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.utils;

import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.spi.PersistenceUnitInfo;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
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
    
    /**
     * Используется для добавления нового CoercionTuple
     */
    public static <S, T> void addTuple(Configuration<CoercionTuple> configuration, Class<S> sourceType, Class<T> targetType,
            Coercion<S, T> coercion) {
        CoercionTuple<S, T> tuple = new CoercionTuple<S, T>(sourceType, targetType, coercion);
        configuration.add(tuple);
    }
    
    public static String getDefaultPersistenceUnitName(EntityManagerSource entityManagerSource)
    {
        List<PersistenceUnitInfo> entityManagers = entityManagerSource.getPersistenceUnitInfos();

        if (entityManagers.size() == 1)
            return entityManagers.get(0).getPersistenceUnitName();

        throw new RuntimeException("Unable to locate a single PersistenceUnitInfo. ");
    }
    
}
