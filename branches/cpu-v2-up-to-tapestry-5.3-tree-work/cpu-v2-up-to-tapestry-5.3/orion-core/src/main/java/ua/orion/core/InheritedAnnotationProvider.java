package ua.orion.core;

import java.lang.annotation.Annotation;
import org.apache.tapestry5.ioc.AnnotationProvider;

/**
 * Расширяет AnnotationProvider информацией о том в каком классе
 * иерархии наследования была объявлена аннотация
 * @author user
 */
public interface InheritedAnnotationProvider extends AnnotationProvider {

    /**
     * Возвращает класс в котором была определена или послейний раз переопределена
     * аннотация
     * @param annotationClass
     * @return
     */
    Class<?> getDeclarationBeanType(Class<? extends Annotation> annotationClass);
}
