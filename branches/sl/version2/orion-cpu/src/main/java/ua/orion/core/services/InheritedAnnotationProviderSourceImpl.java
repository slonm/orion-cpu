/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.services;

import java.lang.annotation.Annotation;
import java.util.*;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import sun.management.jmxremote.ConnectorBootstrap.PropertyNames;

/**
 *
 * @author sl
 */
public class InheritedAnnotationProviderSourceImpl implements InheritedAnnotationProviderSource {

    private final PropertyAccess propertyAccess;
    private final Map<Class<?>, AnnotationProvider> classCache = new HashMap();
    private final Map<Class<?>, AnnotationProvider> classWithInterfacesCache = new HashMap();

    @Override
    public AnnotationProvider getClassProvider(final Class<?> clasz) {
        if (!classCache.containsKey(clasz)) {
            classCache.put(clasz, new AnnotationProvider() {

                @Override
                public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }
        return classCache.get(clasz);
    }

    @Override
    public AnnotationProvider getClassProviderWithInterfaces(Class<?> clasz) {
        if (!classWithInterfacesCache.containsKey(clasz)) {
            classWithInterfacesCache.put(clasz, new AnnotationProvider() {

                @Override
                public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }
        return classWithInterfacesCache.get(clasz);
    }

    @Override
    public AnnotationProvider getPropertyProvider(Class<?> clasz, String propertyName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AnnotationProvider getPropertyProviderWithInterfaces(Class<?> clasz, String propertyName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearCache() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
