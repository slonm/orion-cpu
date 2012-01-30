package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterDataType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * условие "поле не пустое"
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterISNOTNULL extends GridFilterCheckbox {

    public GridFilterISNOTNULL(GridPropertyModelInterface _model) {
        super(_model);
        this.setUid(this.fieldName + "ISNOTNULL");
        this.setLabel(" != null ");
    }

    public GridFilterISNOTNULL(GridPropertyModelInterface _model, String _uid, String _fieldName) {
        super(_model, _uid, _fieldName);
        this.setUid(this.fieldName + "ISNOTNULL");
        this.setLabel(" != null ");
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
    public void setDataType(GridFilterDataType _validator) {
        this.dataType = null;
    }
}
