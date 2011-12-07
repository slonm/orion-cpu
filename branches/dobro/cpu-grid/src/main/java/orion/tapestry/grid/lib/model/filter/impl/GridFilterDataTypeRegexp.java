package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterDataType;

/**
 * Валидатор на основе регулярного выражения
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterDataTypeRegexp implements GridFilterDataType<String> {

    private String pattern;

    GridFilterDataTypeRegexp(String _pattern){
        this.pattern=_pattern;
    }

    @Override
    public boolean isValid(String value) {
        return value.matches(this.pattern);
    }

    @Override
    public String fromString(String value) {
        if (!isValid(value)) {
            return null;
        }
        return value;
    }

    @Override
    public String getJSValidator() {
        return "validatorRegexp";
    }
}
