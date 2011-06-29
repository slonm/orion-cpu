package orion.tapestry.grid.lib.field.filter.datatype;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;

/**
 *
 * @author dobro
 */
public class FieldFilterElementByte implements FieldFilterElementDataType<Byte> {

    @Override
    public boolean isValid(String value) {
        return value.matches("[+-]?[0-9]{1,3}");
    }

    @Override
    public Byte fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new Byte(value.replace('+', ' ').trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }
}
