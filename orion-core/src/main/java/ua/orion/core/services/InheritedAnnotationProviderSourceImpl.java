package ua.orion.core.services;

import java.lang.annotation.Annotation;
import java.util.*;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import ua.orion.core.InheritedAnnotationProvider;

/**
 *
 * @author sl
 */
public class InheritedAnnotationProviderSourceImpl implements InheritedAnnotationProviderSource {

    private final PropertyAccess propertyAccess;
    private final Map<Class<?>, ClassAnnotationProvider> classCache = new HashMap();
    private final Map<Class<?>, ClassAnnotationProvider> classWithInterfacesCache = new HashMap();

    public InheritedAnnotationProviderSourceImpl(PropertyAccess propertyAccess) {
        this.propertyAccess = propertyAccess;
    }

    @Override
    public InheritedAnnotationProvider getClassProvider(final Class<?> clasz) {
        if (!classCache.containsKey(clasz)) {
            classCache.put(clasz, new ClassAnnotationProvider(clasz));
        }
        return classCache.get(clasz);
    }

    @Override
    public InheritedAnnotationProvider getClassProviderWithInterfaces(Class<?> clasz) {
        if (!classWithInterfacesCache.containsKey(clasz)) {
            classWithInterfacesCache.put(clasz, new ClassWithInterfacesAnnotationProvider(clasz));
        }
        return classWithInterfacesCache.get(clasz);
    }

    @Override
    public InheritedAnnotationProvider getPropertyProvider(Class<?> clasz, String propertyName) {
        if (!classCache.containsKey(clasz)) {
            getClassProvider(clasz);
        }
        return classCache.get(clasz).getPropertyProvider(propertyName);
    }

    @Override
    public InheritedAnnotationProvider getPropertyProviderWithInterfaces(Class<?> clasz, String propertyName) {
        if (!classWithInterfacesCache.containsKey(clasz)) {
            getClassProviderWithInterfaces(clasz);
        }
        return classWithInterfacesCache.get(clasz).getPropertyProvider(propertyName);
    }

    @Override
    public void clearCache() {
        classWithInterfacesCache.clear();
        classCache.clear();
    }

    private class AnnotationAndBeanType {

        Annotation annotation;
        Class<?> type;
    }

    private abstract class CachedAnnotationProvider implements InheritedAnnotationProvider {

        protected Map<Class<? extends Annotation>, Annotation> annotationCache = new HashMap();
        protected Map<Class<? extends Annotation>, Class<?>> beanTypeCache = new HashMap();

        @Override
        public final <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            fill(annotationClass);
            return (T) annotationCache.get(annotationClass);
        }

        @Override
        public final Class<?> getDeclarationBeanType(Class<? extends Annotation> annotationClass) {
            fill(annotationClass);
            return beanTypeCache.get(annotationClass);
        }

        private void fill(Class<? extends Annotation> annotationClass) {
            if (!annotationCache.containsKey(annotationClass)) {
                AnnotationAndBeanType pair = getData(annotationClass);
                annotationCache.put(annotationClass, pair.annotation);
                beanTypeCache.put(annotationClass, pair.type);
            }
        }

        protected abstract AnnotationAndBeanType getData(Class<? extends Annotation> annotationClass);
    }

    private class ClassAnnotationProvider extends CachedAnnotationProvider {

        final Class<?> type;
        protected Map<String, InheritedAnnotationProvider> propertyCache = new HashMap();

        public ClassAnnotationProvider(Class<?> type) {
            this.type = type;
        }

        public final InheritedAnnotationProvider getPropertyProvider(String propertyName) {
            if (!propertyCache.containsKey(propertyName)) {
                propertyCache.put(propertyName, newProvider(type, propertyName));
            }
            return propertyCache.get(propertyName);
        }

        protected InheritedAnnotationProvider newProvider(Class<?> type, String propertyName) {
            return new PropertyAnnotationProvider(type, propertyName);
        }

        @Override
        protected AnnotationAndBeanType getData(Class<? extends Annotation> annotationClass) {
            AnnotationAndBeanType aab = new AnnotationAndBeanType();
            aab.annotation = null;
            aab.type = type;
            while (aab.annotation == null && aab.type != null) {
                aab.annotation = aab.type.getAnnotation(annotationClass);
                if (aab.annotation != null) {
                    break;
                }
                aab.type = aab.type.getSuperclass();
            }
            if (aab.annotation == null) {
                aab.type = null;
            }
            return aab;
        }
    }

    private class ClassWithInterfacesAnnotationProvider extends ClassAnnotationProvider {

        public ClassWithInterfacesAnnotationProvider(Class<?> type) {
            super(type);
        }

        @Override
        protected AnnotationAndBeanType getData(Class<? extends Annotation> annotationClass) {
            AnnotationAndBeanType aab = super.getData(annotationClass);
            if (aab.annotation == null) {
                for (Class<?> clasz : type.getInterfaces()) {
                    aab.annotation = clasz.getAnnotation(annotationClass);
                    if (aab.annotation != null) {
                        aab.type = clasz;
                        break;
                    }
                }
            }
            return aab;
        }

        @Override
        protected InheritedAnnotationProvider newProvider(Class<?> type, String propertyName) {
            return new PropertyWithInterfacesAnnotationProvider(type, propertyName);
        }
    }

    private class PropertyAnnotationProvider extends CachedAnnotationProvider {

        final Class<?> type;
        final String propertyName;

        public PropertyAnnotationProvider(Class<?> type, String propertyName) {
            this.type = type;
            this.propertyName = propertyName;
        }

        @Override
        protected AnnotationAndBeanType getData(Class<? extends Annotation> annotationClass) {
            AnnotationAndBeanType aab = new AnnotationAndBeanType();
            aab.annotation = null;
            aab.type = type;
            while (aab.annotation == null && aab.type != null) {
                AnnotationProvider ap = propertyAccess.getAdapter(aab.type).getPropertyAdapter(propertyName);
                if (ap == null) {
                    return aab;
                }
                aab.annotation = ap.getAnnotation(annotationClass);
                if (aab.annotation != null) {
                    break;
                }
                aab.type = aab.type.getSuperclass();
            }
            return aab;
        }
    }

    private class PropertyWithInterfacesAnnotationProvider extends PropertyAnnotationProvider {

        public PropertyWithInterfacesAnnotationProvider(Class<?> type, String propertyName) {
            super(type, propertyName);
        }

        @Override
        protected AnnotationAndBeanType getData(Class<? extends Annotation> annotationClass) {
            AnnotationAndBeanType aab = super.getData(annotationClass);
            if (aab.annotation == null) {
                for (Class<?> clasz : type.getInterfaces()) {
                    AnnotationProvider ap = propertyAccess.getAdapter(clasz).getPropertyAdapter(propertyName);
                    if (ap == null) {
                        continue;
                    }
                    aab.annotation = ap.getAnnotation(annotationClass);
                    if (aab.annotation != null) {
                        aab.type = clasz;
                        break;
                    }
                }
            }
            return aab;
        }
    }
}
