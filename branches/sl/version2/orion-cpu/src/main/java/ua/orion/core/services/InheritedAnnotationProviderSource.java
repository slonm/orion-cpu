/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.services;

import org.apache.tapestry5.ioc.AnnotationProvider;

/**
 * Доступ к унаследованным аннотациям
 * @author sl
 */
public interface InheritedAnnotationProviderSource{

    /**
     * Возвращаемый AnnotationProvider будет искать аннотацию в классе и суперкласах,
     * в обратном порядке наследования и вернет null если не найдет
     * @param clasz
     * @return 
     */
    AnnotationProvider getClassProvider(Class<?> clasz);

    /**
     * Возвращаемый AnnotationProvider будет искать аннотацию в классе и суперкласах,
     * в обратном порядке наследования, затем в интерфейсах реализуемых классом
     * и вернет null если не найдет
     * @param clasz
     * @return 
     */
    AnnotationProvider getClassProviderWithInterfaces(Class<?> clasz);

    AnnotationProvider getPropertyProvider(Class<?> clasz, String propertyName);

    AnnotationProvider getPropertyProviderWithInterfaces(Class<?> clasz, String propertyName);
    
    /**
     * Discards all stored property access information, discarding all created class adapters.
     */
    void clearCache();
}
