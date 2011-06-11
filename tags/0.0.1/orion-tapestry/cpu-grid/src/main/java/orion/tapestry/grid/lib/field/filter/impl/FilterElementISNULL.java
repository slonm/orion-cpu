package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * элемент "Больше чем или равно"
 * @author root
 */
public class FilterElementISNULL extends FilterElementCheckbox {

    public FilterElementISNULL(String newFieldName,String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName+"ISNULL");
        this.setLabel(newLabel);
    }

    @Override
    public <T> boolean modifyRestriction(
            RestrictionEditorInterface<T> restriction,
            Object value,
            boolean isActive,
            int nChildren) throws RestrictionEditorException {
        if(!isActive) return false;
        restriction.constField(this.fieldName);
        restriction.isNull();
        return true;
    }

    @Override
    public void setValidator(FieldFilterElementValidator _validator){
        this.validator=null;
    }
}
