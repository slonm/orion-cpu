package orion.tapestry.grid.lib.field.filter.datatype;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;

/**
 *
 * @author dobro
 */
public class FieldFilterElementBoolean implements FieldFilterElementDataType<Boolean> {

    @Override
    public boolean isValid(String value) {
        return value.matches("yes|no|false|true|0|1");
    }

    @Override
    public Boolean fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return value.matches("yes|true|1");
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }
}
