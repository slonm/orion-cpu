package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Условие "строка похожа на"
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementLIKE extends FilterElementText {

    public FilterElementLIKE(String newFieldName,String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName+"LIKE");
        this.setLabel(newLabel);
    }

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
        if (validator != null) {
            checkedValue = validator.fromString(value.toString());
            if (checkedValue == null) {
                return false;
            }
        }else{
            checkedValue=value;
        }
        restriction.constField(this.fieldName);
        restriction.constValue(checkedValue);
        restriction.like();
        return true;
    }
}
