package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * элемент фильтра, отображаемый как чекбокс
 * @author Gennadiy Dobrovolsky
 */
public abstract class GridFilterCheckbox extends GridFilterAbstract {

    GridFilterCheckbox(GridPropertyModelInterface _model) {
        super(_model);
        this.setGUIType(GridFilterGUIType.CHECKBOX);
    }

    GridFilterCheckbox(GridPropertyModelInterface _model, String _uid, String _fieldName) {
        super(_model, _uid, _fieldName);
        this.setGUIType(GridFilterGUIType.CHECKBOX);
    }
}
