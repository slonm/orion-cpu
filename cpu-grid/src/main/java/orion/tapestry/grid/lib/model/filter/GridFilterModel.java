package orion.tapestry.grid.lib.model.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterAND;
import orion.tapestry.grid.lib.model.filter.impl.GridFilterOR;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Агрегатор фильтров. Отвечает за сборку единого выражения из атомарных элементов.
 *
 * @author Gennadiy Dobrovolsky
 */
public class GridFilterModel {

    private static Logger logger = LoggerFactory.getLogger(GridFilterModel.class);
    /**
     * список возможных элементов фильтра
     */
    private Map<String, GridFilterAbstract> filterElementList;
    /**
     * Модель, которую надо фильтровать
     */
    private GridBeanModel model;

    public GridFilterModel(GridBeanModel _gridBeanModel) {
        model = _gridBeanModel;
        this.filterElementList = new HashMap<String, GridFilterAbstract>();
        List<String> propertyNames = _gridBeanModel.getPropertyNames();
        for (String name : propertyNames) {
            for (GridFilterAbstract f : _gridBeanModel.get(name).getGridFilterList()) {
                this.filterElementList.put(f.getUid(), f);
            }
        }
        this.filterElementList.put("AND", new GridFilterAND(null));
        this.filterElementList.put("OR", new GridFilterOR(null));
    }

    public GridFilterModel(List<GridFilterAbstract> _filterElementList) {
        this.filterElementList = new HashMap<String, GridFilterAbstract>();
        for (GridFilterAbstract f : _filterElementList) {
            this.filterElementList.put(f.getUid(), f);
        }
    }

    /**
     * Возвращает условие фильтрации, скомбинированное из набора фильтров
     * @param filterJSON условие фильтрации в формате JSON, полученное из формы
     * @param restrictionEditor объект-редактор условия фильтрации
     * @throws RestrictionEditorException возникает, если не удалось составить условие фильтрации
     * @throws JSONException возникает, если формат строки filterJSON не удалось распознать
     */
    public void modifyRestriction(
            String filterJSON,
            RestrictionEditorInterface restrictionEditor)
            throws RestrictionEditorException, JSONException {

        // создаём пустое выражение
        restrictionEditor.createEmpty();
        logger.info("void modifyRestriction(" + filterJSON);

        if (filterJSON == null || filterJSON.isEmpty()) {
            return;
        }

        // создаём объект из строки
        JSONObject root = new JSONObject(filterJSON);

        // выполняем операции, описанные объектом filterJSON, в редакторе restrictionEditor
        this.modifyRestriction(root, restrictionEditor);
    }

    /**
     * Изменение условия фильтрации
     */
    public int modifyRestriction(
            JSONObject node,
            RestrictionEditorInterface restrictionEditor)
            throws JSONException, RestrictionEditorException {
        // отмечен ли флажок "условие активно"
        boolean isActive = true;
        isActive = node.optBoolean("isactive", true);
        if (!isActive) {
            return 0;
        }
        // узел имеет тип
        // если узел имеет детей, надо сначала применить правила детей
        // и заодно сосчитать количество модификаций, совершенных детьми.
        int nModifications = 0;
        if (node.has("children")) {
            JSONObject children = node.getJSONObject("children");
            String[] keyList = JSONObject.getNames(children);
            if (keyList != null) {
                for (String key : keyList) {
                    // logger.info(key + "=>" + children.getJSONObject(key));
                    nModifications += this.modifyRestriction(children.getJSONObject(key), restrictionEditor);
                }
            }
        }

        // а потом применить собственные правила узла
        // (в зависимости от модификаций, совершенных детьми)
        // параметры узла : тип узла, строка с данными пользователя, количество детей, активный или нет
        // например, узел "И" имеет детей и не принимает данные пользователя
        // для правильного вычисления, надо знать количество детей
        GridFilterAbstract fElement = this.filterElementList.get(node.getString("typeName"));
        // logger.info(node.getString("typeName")+" = >" + fElement);
        if (fElement != null) {

            // данные пользователя из формы
            Object value = null;
            if (node.has("value")) {
                value = node.getString("value");
            }

            // вызываем метод для модификации условия фильтрации
            if (fElement.modifyRestriction(restrictionEditor, value, isActive, nModifications)) {
                //nModifications++;
                nModifications = 1;
            }
        }
        return nModifications;
    }

    @Override
    public String toString() {
        return getElementBuildersJSON().toString();
    }

    public JSONObject getElementBuildersJSON() {
        JSONObject elementBuildersJSON = new JSONObject();
        for (GridFilterAbstract flt : this.filterElementList.values()) {
            try {
                elementBuildersJSON.put(flt.getUid(), flt.getJSONObject());
            } catch (JSONException ex) {
                // TODO: add logger here
            }
        }
        return elementBuildersJSON;
    }

    public String getElementBuildersJSONString() {
        return getElementBuildersJSON().toString();
    }

    public GridBeanModel getModel() {
        return model;
    }
}
