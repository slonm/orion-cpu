package orion.tapestry.grid.lib.field.filter.datatype;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;

/**
 * Валидатор на основе регулярного выражения
 * @author dobro
 */
public class FieldFilterElementRegexp implements FieldFilterElementDataType<String> {

    private String pattern;

    FieldFilterElementRegexp(String _pattern){
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
        return "validator_require_int";
    }
}
