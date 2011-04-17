package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * элемент "И"
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementAND extends FilterElementAbstract {

    public FilterElementAND(String newLabel) {
        this.setUid("NodeAND");
        this.setLabel(newLabel);
        this.setType(FieldFilterElementType.NodeAND);
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
        if(nChildren==0) return false;
        for (int i = 1; i < nChildren; i++) {
            restriction.and();
        }
        return true;
    }
}
