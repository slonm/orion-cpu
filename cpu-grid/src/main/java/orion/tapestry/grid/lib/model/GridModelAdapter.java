package orion.tapestry.grid.lib.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.chenillekit.google.utils.JSONException;
import org.chenillekit.google.utils.JSONObject;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.field.filter.FilterAggregator;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementAND;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementNOT;
import orion.tapestry.grid.lib.field.filter.impl.FilterElementOR;
import orion.tapestry.grid.lib.field.sort.GridFieldSortComparator;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorInterface;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 * Игрушечная реализация класса-описания списка
 * @param <S> тип выражения, описывающего правила отбора строк
 * @author Gennadiy Dobrovolsky
 */
public abstract class GridModelAdapter<S> implements GridModelInterface {

    /**
     * Список полей
     */
    protected List<GridFieldAbstract> fieldList;
    /**
     * Информация о постраничном показе
     */
    protected Pager pager;
    /**
     * Информация о сортировке строк
     */
    protected ArrayList<GridFieldSort> fieldSortList;
    /**
     * Атомарные элементы фильтра
     */
    protected List<FilterElementAbstract> filterElementList;
    /**
     * Список дополнительных фильтров
     */
    protected List<FilterElementAbstract> fieldFilterListAdditional = new ArrayList<FilterElementAbstract>();

    {
        // операции "И", "ИЛИ", "НЕ"
        fieldFilterListAdditional.add(new FilterElementAND("AND"));
        fieldFilterListAdditional.add(new FilterElementOR("OR"));
        fieldFilterListAdditional.add(new FilterElementNOT("NOT"));
    }
    /**
     * Информация о видимости и порядке столбцов
     */
    protected List<GridFieldView> fieldViewList;
    /**
     * Агрегатор фильтров, который умеет составлять из всех фильтров единое условие
     */
    protected FilterAggregator filterAggregator;
    /**
     * Строка, последовательность команд для редактора фильтров.
     * Предполагается, что это строка в формате JSON.
     */
    protected String filterJSON;
    /**
     * Редактор настоящего условия фильтрации.
     */
    protected RestrictionEditorInterface<S> restrictionEditor;
    /**
     * Редактор, составляющий удобное для понимания условие фильтрации.
     * Это условие будет показано на странице.
     */
    protected RestrictionEditorInterface<String> restrictionEditorHumanReadable;


    // =============================================================================
    // Эти методы надо заменять своими
    /**
     * Метод возвращает набор видимых строк
     * @throws RestrictionEditorException если не удалось создать условие фильтрации
     */
    @Override
    public abstract List<GridRow> getRows() throws RestrictionEditorException;

    // =========================================================================
    // Методы, указанные ниже, скорее всего, перекрывать не придётся никогда
    // =========================================================================
    /**
     * работа с описанием сортировки
     */
    @Override
    public List<GridFieldSort> getFieldSortList() {
        if (this.fieldSortList == null) {
            this.fieldSortList = new ArrayList<GridFieldSort>();
            GridFieldSort fs;
            for (GridFieldAbstract f : this.fieldList) {
                fs = f.getFieldSort();
                if (fs != null) {
                    this.fieldSortList.add(fs);
                }
            }
        }
        Collections.sort(this.fieldSortList, new GridFieldSortComparator());
        return this.fieldSortList;
    }

    /**
     * Устанавливает новые данные о сортировке строк в таблице.
     * Данные о сортировке - это список объектов GridFieldSort.
     * Если UID поля и UID объекта GridFieldSort совпадают,
     * то свойство fieldSort соответствующего поля изменяется,
     * иначе данные игнорируются
     * @param _fieldSortList новые данные о сортировке
     */
    @Override
    public void setFieldSortList(List<GridFieldSort> _fieldSortList) {
        String _uid;
        GridFieldAbstract f;
        for (GridFieldSort fs : _fieldSortList) {
            f = getField(fs.getUid());
            if (f != null) {
                f.setFieldSort(fs);
            }
        }
        this.fieldSortList = null;
        getFieldSortList();
    }

    // =========================================================================
    // работа с описанием видимости полей
    //
    @Override
    public List<GridFieldView> getFieldViewList() {
        if (this.fieldViewList == null) {
            this.fieldViewList = new ArrayList<GridFieldView>();
            GridFieldView fs;
            for (GridFieldAbstract f : this.fieldList) {
                //System.out.println(" >>>>> "+f.getUid());
                fs = f.getFieldView();
                if (fs != null) {
                    this.fieldViewList.add(fs);
                }
            }
        }
        return fieldViewList;
    }

    @Override
    public void setFieldViewList(List<GridFieldView> _fieldViewList) {
        GridFieldAbstract f;
        for (GridFieldView fs : _fieldViewList) {
            f = getField(fs.getUid());
            if (f != null) {
                f.setFieldView(fs);
            }
        }
        this.fieldViewList = null;
        getFieldViewList();
    }

    // =========================================================================
    // работа с полями
    //
    /**
     * Найти поле по значению UID
     * @param uid строковый идентификатор поля
     * @return поле с заданным значением uid или null, если поле не найдено
     */
    @Override
    public GridFieldAbstract getField(String uid) {
        for (GridFieldAbstract f : this.fieldList) {
            if (uid.equals(f.getUid())) {
                return f;
            }
        }
        return null;
    }

    @Override
    public void addField(GridFieldAbstract fld) {
        this.fieldList.add(fld);
    }

    @Override
    public void delField(String uid) {
        this.fieldList.remove(this.getField(uid));
    }

    @Override
    public List<GridFieldAbstract> getFields() {
        return this.fieldList;
    }

    // =========================================================================
    // работа с фильтрами
    //
    @Override
    public void addFilterElement(FilterElementAbstract flt) {
        fieldFilterListAdditional.add(flt);
    }

    @Override
    public List<FilterElementAbstract> getFilterElementList() {
        if (this.filterElementList == null) {
            this.filterElementList = new ArrayList<FilterElementAbstract>();
            // Опрашиваем каждое поле, забираем у него определение фильтра
            // Если вместо фильтра поле возвращает NULL,
            // значит фильтра у этого поля нет
            List<FilterElementAbstract> fa;
            for (GridFieldAbstract f : fieldList) {
                fa = f.getFilterElementList();
                if (fa != null) {
                    this.filterElementList.addAll(fa);
                }
            }

            // Добавляем фильтры из дополнительного списка
            for (FilterElementAbstract flt : this.fieldFilterListAdditional) {
                this.filterElementList.add(flt);
            }
        }
        return this.filterElementList;
    }

    @Override
    public void setFilterElementList(List<FilterElementAbstract> _filterElementList) {
        this.filterElementList = _filterElementList;
    }

    @Override
    public void setFilter(String _filterJSON) {

        this.filterJSON = _filterJSON;
        String customFilter=this.customFilterJSON();
        if(customFilter!=null){
            try {
                JSONObject root = new JSONObject(customFilter);
                this.filterJSON = "{\"type\": \"NodeAND\", \"isactive\": \"1\", \"children\": {\"filterNodeUser\":"+this.filterJSON+",\"filterNodeSys\":"+customFilter+"}}";
            } catch (JSONException ex) {
                Logger.getLogger(GridModelAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getFilter() {
        return this.filterJSON;
    }

    @Override
    public String getHumanReadableFilterInfo() {
        String ret = null;
        try {
            this.filterAggregator.applyRestriction(this.filterJSON, this.restrictionEditorHumanReadable);
            ret = this.restrictionEditorHumanReadable.getValue();
        } catch (RestrictionEditorException ex) {
            Logger.getLogger(GridModelAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    // =========================================================================
    // работа с разбивкой на страницы
    //
    @Override
    public Pager getPager() {
        return this.pager;
    }

    @Override
    public void setPager(Pager _pager) {
        this.pager = _pager;
    }

    @Override
    public String customFilterJSON() {
        // не делает ничего, но можно и поправить условие фильтрации
        return null;
    }
}
