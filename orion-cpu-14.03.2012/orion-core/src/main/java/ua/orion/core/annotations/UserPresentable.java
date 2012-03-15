package ua.orion.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author sl
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface UserPresentable {
    /**
     * Имя UserPresentable атрибута. Используется если аннотирован класс
     */
    String value() default "";
}
