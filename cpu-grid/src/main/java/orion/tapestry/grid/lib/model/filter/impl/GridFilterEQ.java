package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Условие "равно"
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterEQ extends GridFilterText {

    public GridFilterEQ(GridPropertyModelInterface model) {
        super(model);
        this.setUid(this.fieldName + "EQ");
        this.setLabel("=");
    }

    public GridFilterEQ(GridPropertyModelInterface _model, String _uid, String _fieldName) {
        super(_model, _uid, _fieldName);
        this.setUid(_uid + "EQ");
        // this.setUid(this.fieldName + "EQ");
        this.setLabel(" = ");
    }

    /**
     * Условие "равно"
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
        if (dataType != null) {
            checkedValue = dataType.fromString(value.toString());
            if (checkedValue == null) {
                return false;
            }
        } else {
            checkedValue = value;
        }

        //System.out.println(" ===== "+this.fieldName + " == "+checkedValue);
        restriction.constField(this.fieldName);
        restriction.constValue(checkedValue);
        restriction.eq();
        return true;
    }
}
