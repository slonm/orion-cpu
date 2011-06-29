package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;
import orion.tapestry.grid.lib.field.filter.datatype.FieldFilterElementList;
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

    /**
     * Условие "принадлежит множеству"
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
    public void setDatatype(FieldFilterElementDataType _validator) {
        // особенный валидатор для списка
        this.datatype = new FieldFilterElementList(_validator);
    }
}
