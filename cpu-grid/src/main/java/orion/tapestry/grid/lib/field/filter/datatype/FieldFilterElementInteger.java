package orion.tapestry.grid.lib.field.filter.datatype;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;

/**
 *
 * @author dobro
 */
public class FieldFilterElementInteger implements FieldFilterElementDataType<Integer> {

    @Override
    public boolean isValid(String value) {
        return value.matches("[+-]?[0-9]+");
    }

    @Override
    public Integer fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new Integer(value.replace('+', ' ').trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_int";
    }

    public static void main(String [] a){
        FieldFilterElementInteger v=new FieldFilterElementInteger();
        System.out.println(v.isValid("23e"));
        System.out.println(v.isValid("re23e"));
        System.out.println(v.isValid("+23"));
        System.out.println(v.isValid("-23"));
        System.out.println(v.isValid("1222222222222222222444444444444444444444444444444444444444444444"));
        System.out.println(v.fromString("23e"));
        System.out.println(v.fromString("re23e"));
        System.out.println(v.fromString("+23"));
        System.out.println(v.fromString("-23"));
        System.out.println(v.fromString("1222222222222222222444444444444444444444444444444444444444444444"));
    }
}
