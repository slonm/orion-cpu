package orion.tapestry.grid.lib.field.filter.validator;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;

/**
 *
 * @author dobro
 */
public class ValidatorRequireLong implements FieldFilterElementValidator<Long> {

    @Override
    public boolean isValid(String value) {
        return value.matches("[+-]?[0-9]{1,3}");
    }

    @Override
    public Long fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new Long(value.replace('+', ' ').trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }
}
