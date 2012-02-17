package ua.orion.core.persistence;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Маркер справочника.
 * @author sl
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface ReferenceBook {
}
