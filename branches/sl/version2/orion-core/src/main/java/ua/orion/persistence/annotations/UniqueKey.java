package ua.orion.persistence.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Аннотация может переопределятся при наследовании.
 * @author sl
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface UniqueKey {
    /**
     * Имя UniqueKey атрибута. Используется если аннотирован класс
     */
    String value() default "";
}
