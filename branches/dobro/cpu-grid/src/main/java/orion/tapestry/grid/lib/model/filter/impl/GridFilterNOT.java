package orion.tapestry.grid.lib.model.filter.impl;


import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.filter.GridFilterAbstract;
import orion.tapestry.grid.lib.model.filter.GridFilterGUIType;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * элемент "ИЛИ"
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterNOT extends GridFilterAbstract {

    public GridFilterNOT(GridPropertyModelInterface _model) {
        super(_model);
        this.setUid("NodeNOT");
        this.setLabel(" NOT ");
        this.setGUIType(GridFilterGUIType.NodeNOT);
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
            result.put("guitype", "GridFilterNOT");
        } catch (JSONException ex) {
            // TODO: add logger here
        }
        return result.toString();
    }
}
