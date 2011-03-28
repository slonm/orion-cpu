package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementValidator;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * элемент "Больше чем или равно"
 * @author root
 */
public class FilterElementISNOTNULL extends FilterElementCheckbox {

    public FilterElementISNOTNULL(String newFieldName,String newLabel) {
        super(newFieldName);
        this.setUid(this.fieldName+"ISNOTNULL");
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
        restriction.isNotNull();
        return true;
    }

    @Override
    public void setValidator(FieldFilterElementValidator _validator){
        this.validator=null;
    }
}
