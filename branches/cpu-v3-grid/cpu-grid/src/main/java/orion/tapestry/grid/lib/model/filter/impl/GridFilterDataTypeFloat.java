package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterDataType;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterDataTypeFloat implements GridFilterDataType<Float> {

    @Override
    public boolean isValid(String value) {
        return value.replace(',','.').matches("(?i)[+-]?(\\d+|\\d+\\.\\d*|\\d*\\.\\d+)(e[+-]?[0-9]+)?");
    }

    @Override
    public Float fromString(String value) {
        if (!isValid(value)) return null;
        try{
            return new Float(value.replace('+', ' ').replace(',','.').trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    @Override
    public String getJSValidator() {
        return "validatorFloat";
    }

    public static void main(String [] a){
        GridFilterDataTypeFloat v=new GridFilterDataTypeFloat();
        String[] validateMe={"12","+1.5","-.89","gg","erw","1e9","3.2344E4","45,67"};
        for(String s: validateMe){
            System.out.println(s+" "+v.isValid(s));
            System.out.println(s+" => "+v.fromString(s));
        }
    }
}
