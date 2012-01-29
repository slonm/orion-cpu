package ua.orion.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Валидация бина на уникальности в соответствии с аннотациями JPA
 * Column, JoinColumn, AttributeOverride, AttributeOverides, AssociationOverride,
 * AssociationOverrides.
 * Аннотация не имеет специфических опций.
 * Для того что-бы валидация работала необходима инициализация валидатора
 * {@see UniqueConstraintValidator}
 * @author sl
 */
@Target({ TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {UniqueConstraintValidator.class})
public @interface Unique {
	String message() default UniqueConstraintValidator.MESSAGE;

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default {};
}
