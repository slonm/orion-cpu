package orion.tapestry.grid.lib.field.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;

/**
 * Агрегатор фильтров. Отвечает за сборку единого выражения из атомарных элементов.
 *
 * @author Gennadiy Dobrovolsky
 */
public class FilterAggregator {

    /**
     * список возможных элементов фильтра
     */
    private Map<String, FilterElementAbstract> filterElementList;

    public FilterAggregator(List<FilterElementAbstract> _filterElementList) {
        this.filterElementList = new HashMap<String, FilterElementAbstract>();
        for (FilterElementAbstract f : _filterElementList) {
            this.filterElementList.put(f.getUid(), f);
        }
    }

    /**
     * Возвращает условие фильтрации, скомбинированное из набора фильтров
     * @param filterJSON условие фильтрации в формате JSON, полученное из формы
     * @param restrictionEditor объект-редактор условия фильтрации
     * @throws RestrictionEditorException возникает, если не удалось составить условие фильтрации
     */
    public void applyRestriction(
            String filterJSON,
            RestrictionEditorInterface restrictionEditor)
            throws RestrictionEditorException {
        try {
            // создаём пустое выражение
            restrictionEditor.createEmpty();

            if (filterJSON == null || filterJSON.isEmpty()) {
                return;
            }

            // создаём объект из строки
            System.out.println("filterJSON="+filterJSON);
            JSONObject root = new JSONObject(filterJSON);

            // выполняем операции, описанные объектом root, в редакторе restrictionEditor
            this.modifyRestriction(root, restrictionEditor);
        } catch (JSONException ex) {
            //Logger.getLogger(FilterAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public <T> int modifyRestriction(JSONObject node, RestrictionEditorInterface<T> restrictionEditor) throws JSONException, RestrictionEditorException {
        // отмечен ли флажок "условие активно"
        boolean isActive = true;
        if (node.has("isactive")) {
            isActive = node.getString("isactive").startsWith("1");
        }

        if(!isActive) return 0;

        //System.out.println(node.toString());

        // узел имеет тип
        // если узел имеет детей, надо сначала применить правила детей
        // и заодно сосчитать количество модификаций, совершенных детьми.
        int nModifications = 0;
        if (node.has("children")) {
            JSONObject children = node.getJSONObject("children");
            String[] keyList = JSONObject.getNames(children);
            if (keyList != null) {
                for (String key : keyList) {
                    nModifications += this.modifyRestriction(children.getJSONObject(key), restrictionEditor);
                }
            }
        }
        // а потом применить собственные правила узла
        // (в зависимости от модификаций, совершенных детьми)
        // параметры узла : тип узла, строка с данными пользователя, количество детей, активный или нет
        // например, узел "И" имеет детей и не принимает данные пользователя
        // для правильного вычисления, надо знать количество детей
        FilterElementAbstract fElement = this.filterElementList.get(node.getString("type"));
        //System.out.println(node.getString("type"));
        if (fElement != null) {

            // данные пользователя из формы
            Object value = null;
            if (node.has("value")) {
                value = node.getString("value");
            }

            // вызываем метод для модификации условия фильтрации
            if (fElement.modifyRestriction(restrictionEditor, value, isActive, nModifications)) {
                //nModifications++;
                nModifications=1;
            }
        }
        return nModifications;
    }
}
