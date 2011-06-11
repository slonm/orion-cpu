/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.services;

import java.util.HashMap;
import java.util.Map;
import org.apache.tapestry5.ioc.ObjectLocator;
import ua.orion.core.EntityOrientedBeanFactory;

/**
 *
 * @author sl
 */
public class StringValueProviderImpl implements StringValueProvider {

    private EntityOrientedBeanFactory factory;
    private Map<Class<?>, StringValueProvider> map = new HashMap();

    public StringValueProviderImpl(ModelLibraryService mls,
            ObjectLocator locator) {
        factory = new EntityOrientedBeanFactory(mls, locator, "stringvalues", null, "StringValueProvider");
    }

    @Override
    public String getStringValue(Object entity) {
        Class clas = entity.getClass();
        while (clas != null) {
            if (map.containsKey(clas)) {

                StringValueProvider provider = map.get(clas);
                if (provider != null) {
                    return provider.getStringValue(entity);
                }
            } else {
                StringValueProvider provider = factory.create(StringValueProvider.class, clas);
                map.put(entity.getClass(), provider);
                if (provider != null) {
                    return provider.getStringValue(entity);
                }
            }
            clas = clas.getSuperclass();
        }
        return String.valueOf(entity);
    }
}
