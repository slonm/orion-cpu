/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.core.services;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
/**
 *
 * @author sl
 */
@Target(
        { PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
public @interface OrionCore {

}
