package ua.orion.core.annotations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Определяет порядок создания бинов библиотек.
 * @author sl
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface OrderLibrary {
    String[] value();
}
