package orion.tapestry.grid.lib.field.filter.datatype;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;

/**
 *
 * @author dobro
 */
public class FieldFilterElementDouble implements FieldFilterElementDataType<Double> {

    @Override
    public boolean isValid(String value) {
        return value.replace(',','.').matches("(?i)[+-]?(\\d+|\\d+\\.\\d*|\\d*\\.\\d+)(e[+-]?[0-9]+)?");
    }

    @Override
    public Double fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new Double(value.replace('+', ' ').replace(',','.').trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validator_require_float";
    }

    public static void main(String [] a){
        FieldFilterElementDouble v=new FieldFilterElementDouble();
        String[] validateMe={"12","+1.5","-.89","gg","erw","1e9","3.2344E4","45,67"};
        for(String s: validateMe){
            System.out.println(s+" "+v.isValid(s));
            System.out.println(s+" => "+v.fromString(s));
        }
    }
}
