package orion.tapestry.grid.lib.field.filter.validator;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;

/**
 *
 * @author dobro
 */
public class ValidatorRequireShort implements FieldFilterElementValidator<Short> {

    @Override
    public boolean isValid(String value) {
        return value.matches("[+-]?[0-9]{1,3}");
    }

    @Override
    public Short fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new Short(value.replace('+', ' ').trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }
}
