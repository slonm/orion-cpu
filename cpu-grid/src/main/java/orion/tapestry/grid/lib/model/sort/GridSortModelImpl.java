package orion.tapestry.grid.lib.model.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.grid.SortConstraint;
import org.json.JSONException;
import org.json.JSONObject;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.property.GridPropertyModelInterface;

/**
 * Контейнер для всех правил сортировки
 * @author dobro
 */
public class GridSortModelImpl implements GridSortModel {

    /**
     * список правил сортировки
     */
    List<GridSortConstraint> sortConstraints = new ArrayList<GridSortConstraint>();
    /**
     * Модель, которую надо отображать
     */
    private GridBeanModel model;

    public GridSortModelImpl(GridBeanModel _model) {
        // запоминаем модель записи
        model = _model;
        // сохраняем указатели на правила сортировки, которые хранятся в модели
        for (Object m : _model.getPropertyNames()) {
            GridSortConstraint modelSortConstraint = ((GridPropertyModelInterface) _model.get(m.toString())).getSortConstraint();
            if (modelSortConstraint != null) {
                sortConstraints.add(modelSortConstraint);
            }
        }
        Collections.sort(sortConstraints, new GridSortComparator());
    }

    /**
     * По имени колонки возвращает правило сортировки
     */
    @Override
    public ColumnSort getColumnSort(String columnId) {
        for (GridSortConstraint gsc : this.sortConstraints) {
            if (gsc.getPropertyModel().getId().equals(columnId)) {
                return gsc.getColumnSort();
            }
        }
        return null;
    }

    /**
     * Изменяет условие сортировки для заданной колонки по правилу
     * ColumnSort.ASCENDING -> ColumnSort.DESCENDING -> ColumnSort.UNSORTED -> ColumnSort.ASCENDING
     */
    @Override
    public void updateSort(String columnId) {
        for (GridSortConstraint gsc : this.sortConstraints) {
            if (gsc.getPropertyModel().getId().equals(columnId)) {
                ColumnSort cs = gsc.getColumnSort();
                if (cs.equals(ColumnSort.ASCENDING)) {
                    gsc.setColumnSort(ColumnSort.DESCENDING);
                } else {
                    if (cs.equals(ColumnSort.DESCENDING)) {
                        gsc.setColumnSort(ColumnSort.UNSORTED);
                    } else {
                        gsc.setColumnSort(ColumnSort.ASCENDING);
                    }
                }
            }
        }
    }

    /**
     * Возвращает список всех условий сортировки
     * (метод реализован ради совместимости со старыми версиями)
     */
    @Override
    public List<SortConstraint> getSortConstraints() {
        return new ArrayList<SortConstraint>(sortConstraints);
    }

    /**
     * Возвращает список всех условий сортировки
     */
    public List<GridSortConstraint> getGridSortConstraints() {
        return sortConstraints;
    }

    /**
     * Отменяет все условия сортировки
     */
    @Override
    public void clear() {
        for (GridSortConstraint gsc : this.sortConstraints) {
            gsc.setColumnSort(ColumnSort.UNSORTED);
        }
    }

    /**
     * экспорт всех условий сортировки в формат JSON
     */
    public String exportJSONString() {
        JSONObject sortJSON = new JSONObject();
        for (GridSortConstraint sc : sortConstraints) {
            if (sc.getPropertyModel().isSortable()) {
                try {
                    sortJSON.put(sc.getPropertyModel().getId(), sc.exportJSON());
                } catch (JSONException ex) {
                    // TODO: add logger here
                }
            }
        }
        return sortJSON.toString();
    }

    /**
     * Создать новый набор условий сортировки из строки в формате JSON
     */
    public static GridSortModelImpl importJSONString(GridBeanModel model, String sortJSONString) {

        // Create default sort model
        GridSortModelImpl gridSortModel = new GridSortModelImpl(model);
        try {
            // load JSON string
            JSONObject sortJSON = new JSONObject(sortJSONString);

            // create empty list of the sortConstraints
            gridSortModel.sortConstraints = new ArrayList<GridSortConstraint>();

            // update all property models
            for (Object m : model.getPropertyNames()) {

                // get property model
                GridPropertyModelInterface propertyModel = (GridPropertyModelInterface) model.get(m.toString());

                // check if gridSortConstraint is already set
                GridSortConstraint gridSortConstraint = propertyModel.getSortConstraint();
                
                // create new default GridSortConstraint
                if (gridSortConstraint == null) {
                    // create new default GridSortConstraint
                    gridSortConstraint = new GridSortConstraint(propertyModel, ColumnSort.UNSORTED, 0);

                    // update property model
                    propertyModel.setSortConstraint(gridSortConstraint);
                }

                // identifier of the property model
                String id = propertyModel.getId();

                // load parameters from JSON
                if (sortJSON.has(id)) {
                    gridSortConstraint.importJSON(sortJSON.getJSONObject(id));
                }

                // update list of the sortConstraints
                gridSortModel.sortConstraints.add(gridSortConstraint);
            }

        } catch (JSONException ex) {
            //Logger.getLogger(GridSortModelImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException npe) {
        }
        Collections.sort(gridSortModel.sortConstraints, new GridSortComparator());
        return gridSortModel;
    }

    @Override
    public String toString() {
        return this.exportJSONString();
    }

    public GridBeanModel getModel() {
        return model;
    }
}
