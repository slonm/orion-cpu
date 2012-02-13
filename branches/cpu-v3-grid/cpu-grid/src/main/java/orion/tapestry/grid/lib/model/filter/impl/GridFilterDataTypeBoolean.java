package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterDataType;



/**
 * Проверка и/или преобразование строки к нужному типу данных.
 * Строка вводится пользователем в форме фильтрации.
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterDataTypeBoolean implements GridFilterDataType<Boolean> {

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
        return "validatorBoolean";
    }
}
