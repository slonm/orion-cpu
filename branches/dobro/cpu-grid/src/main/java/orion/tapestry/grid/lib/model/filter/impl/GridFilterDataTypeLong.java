package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterDataType;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterDataTypeLong implements GridFilterDataType<Long> {

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
        return "validatorInt";
    }
}
