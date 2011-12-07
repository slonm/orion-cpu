package orion.tapestry.grid.lib.datasource.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.tapestry5.grid.SortConstraint;
import org.json.JSONException;
import orion.tapestry.grid.lib.datasource.DataSourceAdapter;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.model.filter.GridFilterModel;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorJPACriteria;

/**
 *
 * @author dobro
 */
public class JPADataSource extends DataSourceAdapter {

    private EntityManager entityManager;
    private RestrictionEditorInterface restrictionEditor;
    private int availableRows = 0;
    private List foundRows;
    private Class forClass;
    private GridBeanModel gridBeanModel;
    private int startIndex;

    public JPADataSource(GridBeanModel _gridBeanModel, EntityManager _entityManager, RestrictionEditorInterface _restrictionEditor) {
        this.entityManager = _entityManager;
        this.restrictionEditor = _restrictionEditor;
        this.gridBeanModel = _gridBeanModel;
        this.forClass = gridBeanModel.getBeanType();
    }

    @Override
    public Object getRowValue(int index) {
        if(this.foundRows.size() == 0) {
            return null;
        }
        if (index < 0) {
            return this.foundRows.get(0);
        }
        // System.out.println("getRowValue(int index="+index);
        return this.foundRows.get(index - this.startIndex);
    }

    @Override
    public Class getRowType() {
        return forClass;
    }

    @Override
    public void prepare(int _startIndex, int endIndex, List<SortConstraint> sortConstraints, String filterJSON) {

        this.startIndex = _startIndex;

        // System.out.println(String.format("prepare(int startIndex=%s, int endIndex=%s, List<SortConstraint> sortConstraints, String filterJSON=%s, GridBeanModel gridBeanModel)", _startIndex, endIndex, filterJSON));
        if (this.restrictionEditor == null) {
            this.restrictionEditor = new RestrictionEditorJPACriteria(gridBeanModel.getBeanType(), this.entityManager);
        }


        // create criteria builder
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        Root from;

        // ---------- вычисляем количество найденных строк - begin -------------
        // apply filter if model is set
        if (gridBeanModel != null && filterJSON != null) {
            GridFilterModel filterModel = gridBeanModel.getGridFilterModel();
            try {
                filterModel.modifyRestriction(filterJSON, this.restrictionEditor);
            } catch (RestrictionEditorException ex) {
            } catch (JSONException ex) {
            }
        }
        try {
            // получаем сформированный запрос
            CriteriaQuery nRowsQuery = (CriteriaQuery) this.restrictionEditor.getValue();
            // получаем корень
            from = (Root) nRowsQuery.getRoots().iterator().next();
            // указываем, что будем вычислять количество строк
            nRowsQuery.select(criteriaBuilder.count(from));
            // применяем дополнительные условия
            applyAdditionalConstraints(nRowsQuery);
            // получаем количество строк
            Long result = (Long) this.entityManager.createQuery(nRowsQuery).getSingleResult();
            this.availableRows = result.intValue();
        } catch (RestrictionEditorException ex) {
            // Logger.getLogger(JPADataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        // ---------- вычисляем количество найденных строк - end ---------------

        try {
            // ---------- выбираем строки - begin ------------------------------
            this.restrictionEditor.createEmpty();
            if (gridBeanModel != null && filterJSON != null) {
                GridFilterModel filterModel = gridBeanModel.getGridFilterModel();
                try {
                    filterModel.modifyRestriction(filterJSON, this.restrictionEditor);
                } catch (RestrictionEditorException ex) {
                } catch (JSONException ex) {
                }
            }

            CriteriaQuery query = (CriteriaQuery) this.restrictionEditor.getValue();
            // здесь можно добавить дополнительное условие отбора
            applyAdditionalConstraints(query);

            // получаем корень запроса
            from = (Root) query.getRoots().iterator().next();

            // ---------- используем условие сортировки - begin ----------------
            List ordering = new ArrayList();
            for (SortConstraint fs : sortConstraints) {
                switch (fs.getColumnSort()) {
                    case ASCENDING:
                        ordering.add(criteriaBuilder.asc(from.get(fs.getPropertyModel().getPropertyName())));
                        break;
                    case DESCENDING:
                        ordering.add(criteriaBuilder.desc(from.get(fs.getPropertyModel().getPropertyName())));
                        break;
                }
            }
            query.orderBy(ordering);
            // ---------- используем условие сортировки - end ------------------
            // Выбираем страницу с заданным номером
            TypedQuery typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(_startIndex).setMaxResults(endIndex - _startIndex + 1);

            // выбираем строки из БД
            this.foundRows = typedQuery.getResultList();
            // ---------- выбираем строки - end --------------------------------


        } catch (RestrictionEditorException ex) {
            // Logger.getLogger(JPADataSource.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public int getAvailableRows() {
        return this.availableRows;
    }

    /**
     * Применяет дополнительные условия выборки строк
     * этот метод следует перекрывать
     * @param criteria
     */
    protected void applyAdditionalConstraints(CriteriaQuery criteria) {
    }
}
