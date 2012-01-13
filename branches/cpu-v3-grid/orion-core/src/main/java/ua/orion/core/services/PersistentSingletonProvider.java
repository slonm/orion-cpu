package ua.orion.core.services;

import java.io.Serializable;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.ObjectProvider;
import ua.orion.core.annotations.PersistentSingleton;

/**
 * Поддержка иньектирования хранимых переменых с помощью аннотации
 * PersistentSingleton
 * @author sl
 */
public class PersistentSingletonProvider implements ObjectProvider{

    private final PersistentSingletonSource persistentSingletonSource;

    public PersistentSingletonProvider(@OrionCore PersistentSingletonSource persistentSingletonSource) {
        this.persistentSingletonSource = persistentSingletonSource;
    }
            
    @Override
    public <T> T provide(Class<T> objectType, AnnotationProvider annotationProvider, ObjectLocator locator) {
        PersistentSingleton annotation = annotationProvider.getAnnotation(PersistentSingleton.class);

        if (annotation == null) return null;

        return (T) persistentSingletonSource.get((Class<? extends Serializable>) objectType, annotation.value());
    }

}
