package orion.tapestry.grid.lib.model.filter.impl;


import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Объединяет условия фильтрации с помощью логического ИЛИ
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterOR extends GridFilterAbstract {

    public GridFilterOR(GridPropertyModelInterface _model) {
        //super(_model);
        //this.setUid("OR");
        // this.setLabel("OR ");
        this.setGUIType(GridFilterGUIType.NodeOR);
    }

    /**
     * Объединяет условия фильтрации с помощью логического ИЛИ
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
        if (nChildren == 0) {
            return false;
        }
        for (int i = 1; i < nChildren; i++) {
            restriction.or();
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
        JSONObject result=new JSONObject();
        try {
            result.put("uid", this.getUid());
            result.put("label", this.getLabel());
            result.put("validator", "");
            result.put("guitype", "GridFilterOR");
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
        return "OR";
    }
}
