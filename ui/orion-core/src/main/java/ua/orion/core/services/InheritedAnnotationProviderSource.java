package ua.orion.core.services;

import ua.orion.core.InheritedAnnotationProvider;

/**
 * Доступ к унаследованным аннотациям. Аннотации классой и суперклассов приоритетнее 
 * интерфейсов. При наследовании аннотаций от множества интерфейсов
 * результат неопределенный.
 * @author sl
 */
public interface InheritedAnnotationProviderSource{

    /**
     * Возвращаемый InheritedAnnotationProvider будет искать аннотацию в классе и суперкласах,
     * в обратном порядке наследования и вернет null если не найдет
     * @param clasz
     * @return 
     */
    InheritedAnnotationProvider getClassProvider(Class<?> clasz);

    /**
     * Возвращаемый InheritedAnnotationProvider будет искать аннотацию в классе и суперкласах,
     * в обратном порядке наследования, затем в интерфейсах реализуемых классом
     * и вернет null если не найдет
     * @param clasz
     * @return 
     */
    InheritedAnnotationProvider getClassProviderWithInterfaces(Class<?> clasz);

    /**
     * Возвращаемый InheritedAnnotationProvider будет искать аннотацию в классе и суперкласах,
     * в обратном порядке наследования и вернет null если не найдет
     * @param clasz
     * @return
     */
    InheritedAnnotationProvider getPropertyProvider(Class<?> clasz, String propertyName);

    /**
     * Возвращаемый InheritedAnnotationProvider будет искать аннотацию в классе и суперкласах,
     * в обратном порядке наследования, затем в интерфейсах реализуемых классом
     * и вернет null если не найдет
     * @param clasz
     * @return
     */
    InheritedAnnotationProvider getPropertyProviderWithInterfaces(Class<?> clasz, String propertyName);
    
    /**
     * Discards all stored property access information, discarding all created class adapters.
     */
    void clearCache();
}
