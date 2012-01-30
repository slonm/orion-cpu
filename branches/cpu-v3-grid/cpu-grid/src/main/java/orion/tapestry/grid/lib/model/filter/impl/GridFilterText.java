package orion.tapestry.grid.lib.model.filter.impl;

import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * элемент фильтра, отображаемый как текстовое поле
 * @author Gennadiy Dobrovolsky
 */
public abstract class GridFilterText extends GridFilterAbstract {

    public GridFilterText(GridPropertyModelInterface model) {
        super(model);
        this.setGUIType(GridFilterGUIType.TEXT);
    }

    public GridFilterText(GridPropertyModelInterface _model, String _uid, String _fieldName) {
        super(_model, _uid, _fieldName);
        this.setGUIType(GridFilterGUIType.TEXT);
    }
}
