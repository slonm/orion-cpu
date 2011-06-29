package orion.tapestry.grid.lib.field.filter.impl;

import orion.tapestry.grid.lib.field.filter.FieldFilterElementGUIType;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * элемент "ИЛИ"
 * @author Gennadiy Dobrovolsky
 */
public class FilterElementNOT extends FilterElementAbstract {

    public FilterElementNOT(String newLabel) {
        this.setUid("NodeNOT");
        this.setLabel(newLabel);
        this.setType(FieldFilterElementGUIType.NodeNOT);
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
        if(nChildren==0){
            return false;
        }
        if(nChildren>1){
            throw new RestrictionEditorException();
        }
        //restriction.not();
        return true;
    }
}
