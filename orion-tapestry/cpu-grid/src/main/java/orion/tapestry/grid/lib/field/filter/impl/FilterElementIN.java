package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;
import orion.tapestry.grid.lib.field.filter.validator.ValidatorRequireList;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Условие "принадлежит множеству"
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementIN extends FilterElementText {

    public FilterElementIN(String newFieldName, String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName + "IN");
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
        Object[] valueList = checkedValue.toString().split(",");
        restriction.constValueList(valueList);
        restriction.constField(this.fieldName);
        restriction.in();
        return true;
    }

    /**
     * Устанавливает новый объект в качестве валидатора
     * @param _validator объект-валидатор пользовательского ввода
     */
    @Override
    public void setValidator(FieldFilterElementValidator _validator) {
        // особенный валидатор для списка
        this.validator = new ValidatorRequireList(_validator);
    }
}
