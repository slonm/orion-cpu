package ua.orion.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Иньектирует хранимуюй переменную
 * @author sl
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface PersistentSingleton {
    /**
     * Ключ переменной
     */
    String value();
}
