package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterDataType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * элемент "поле пустое"
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterISNULL extends GridFilterCheckbox {

    public GridFilterISNULL(GridPropertyModelInterface _model) {
        super(_model);
        this.setUid(this.fieldName + "ISNULL");
        this.setLabel("=null");
    }

    public GridFilterISNULL(GridPropertyModelInterface _model, String _uid, String _fieldName) {
        super(_model, _uid, _fieldName);
        this.setUid(this.fieldName + "ISNOTNULL");
        this.setLabel(" != null ");
    }

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
        restriction.isNull();
        return true;
    }

    @Override
    public void setDataType(GridFilterDataType _validator) {
        this.dataType = null;
    }
}
