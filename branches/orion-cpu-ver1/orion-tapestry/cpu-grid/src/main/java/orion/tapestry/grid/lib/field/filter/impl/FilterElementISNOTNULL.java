package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * условие "поле не пустое"
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementISNOTNULL extends FilterElementCheckbox {

    public FilterElementISNOTNULL(String newFieldName, String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName + "ISNOTNULL");
        this.setLabel(newLabel);
    }

    /**
     * условие "поле не пустое"
     */
    @Override
    public <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException {
        if (!isActive) {
            return false;
        }
        restriction.constField(this.fieldName);
        restriction.isNotNull();
        return true;
    }

    /**
     * Валидатора нет
     */
    @Override
    public void setDatatype(FieldFilterElementDataType _validator) {
        this.datatype = null;
    }
}
