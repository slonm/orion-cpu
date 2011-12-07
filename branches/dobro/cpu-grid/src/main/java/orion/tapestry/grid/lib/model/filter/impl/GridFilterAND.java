package orion.tapestry.grid.lib.model.filter.impl;

import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Объединяет условия фильтрации с помощью логического И
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterAND extends GridFilterAbstract {

    public GridFilterAND(GridPropertyModelInterface _model) {

        //this.setLabel("AND ");
        this.setGUIType(GridFilterGUIType.NodeAND);
    }

    /**
     * Объединяет условия фильтрации с помощью логического И
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
        if (nChildren < 2) {
            return false;
        }
        for (int i = 1; i < nChildren; i++) {
            restriction.and();
        }
        return true;
    }

    @Override
    public String getJSONString() {
        // Строка JSON, которая представляет javascript-конструктор для этого элемента формы
        // uid - уникальный идентификатор этого элемента
        // label - название элемента
        // validator - имя валидатора или null
        // guitype - название типа элемента формы
        JSONObject result = new JSONObject();
        try {
            result.put("uid", this.getUid());
            result.put("label", this.getLabel());
            result.put("validator", "");
            result.put("guitype", "GridFilterAND");
        } catch (JSONException ex) {
            // TODO: add logger here
        }
        return result.toString();
    }

    /**
     * @return возвращает текстовую метку элемента формы c названием атрибута
     */
    @Override
    public String getLabel() {
        return "AND";
    }
}
