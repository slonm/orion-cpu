package ua.orion.core.validation;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ValidationException;

/**
 * Фабрика инициализирует UniqueConstraintValidator
 * @author sl
 */
public class UniqueConstraintValidatorFactory implements ConstraintValidatorFactory {

    public final EntityManager entityManager;

    public UniqueConstraintValidatorFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        try {
            T instance=key.newInstance();
            if(instance instanceof UniqueConstraintValidator){
                UniqueConstraintValidator uValidator=(UniqueConstraintValidator)instance;
                uValidator.setEntityManager(entityManager);
            }
            return instance;
        } catch (Exception ex) {
            throw new ValidationException( "Unable to instantiate : " + key.getName() );
        }
    }
}
