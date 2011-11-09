package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Условие "строка содержит подстроку"
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementCONTAINS extends FilterElementText {

    public FilterElementCONTAINS(String newFieldName, String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName + "CONTAINS");
        this.setLabel(newLabel);
    }

    /**
     * Условие "строка содержит подстроку"
     */
    @Override
    public <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException {
        // элемент должен быть активным
        if (!isActive) {
            return false;
        }

        // значение должно существовать
        if (value == null) {
            return false;
        }

        // используем валидатор для проверки данных
        Object checkedValue;
        if (datatype != null) {
            checkedValue = datatype.fromString(value.toString());
            if (checkedValue == null) {
                return false;
            }
        } else {
            checkedValue = value;
        }
        restriction.constField(this.fieldName);
        restriction.constValue(checkedValue);
        restriction.contains();
        return true;
    }
}