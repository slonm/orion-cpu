package ua.orion.web;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import orion.tapestry.grid.services.GridFieldFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import orion.tapestry.grid.lib.field.filter.FilterAggregator;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.model.GridModelAdapter;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorHumanReadable;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.lib.rows.impl.GridRowMap;

/**
 * Конструирует модель из JPABean
 * @author Gennadiy Dobrovolsky
 */
public class GridModelJPABean<T> extends GridModelAdapter<CriteriaQuery<T>> {

    /**
     * Класс сущности
     */
    private Class<T> forClass;
    /**
     * Конфигурация, отображение стандартных типов Java в типы полей
     */
    private Map<String, Class> configuration;
    private EntityManager em;
    private CriteriaBuilder cb;
    protected RestrictionEditorJPACriteria restrictionEditor;
    /**
     * Адаптер для извлечения полей записи
     */
    private ClassPropertyAdapter сlassPropertyAdapter;

    /**
     * Конструктор составляет список полей таблицы
     * и задаёт их начальные свойства
     * @param _forClass класс-сущность для Hibernate
     * @param _configuration конфигурация, соответствие тип атрибута &lt;=&gt; тип колонки в таблице
     * @param em EntityManager object
     * @throws IntrospectionException происходит, если классу GridFieldFactory не удалось понять структуру класса forClass
     */
    public GridModelJPABean(
            Class _forClass,
            Map<String, Class> _configuration,
            GridFieldFactory gridFieldFactory,
            PropertyAccess pa,
            EntityManager entityManager) throws IntrospectionException {

        this.forClass = _forClass;
        this.configuration = _configuration;
        this.em = entityManager;
        cb = em.getCriteriaBuilder();

        // =========== объявляем поля таблицы - начало =========================
        // поля надо объявлять обязательно, а то в таблице не будет колонок
        this.fieldList = gridFieldFactory.getFields(forClass, configuration);
        // =========== объявляем поля таблицы - конец ==========================

        /*
         * Создаём агрегатор фильтров -
         * объект, который хранит редактор фильтров
         * и умеет составлять из набора фильтров единое условие фильтрации
         *
         * агрегатор используется при извлечении строк из базы данных
         */
        this.filterAggregator = new FilterAggregator(this.getFilterElementList());

        /**
         * этот агрегатор используется для отображения условия фильтрации
         */
        this.restrictionEditorHumanReadable = new RestrictionEditorHumanReadable();

        /*
         * Создаём объект,  содержащий информацию о разбивке списка на страницы
         */
        this.pager = new Pager();
        this.pager.setRowsPerPage(25);
        this.pager.setVisiblePage(1);

        /**
         * Адаптер для извлечения полей записи
         */
        this.сlassPropertyAdapter = pa.getAdapter(forClass);
    }

    /**
     * Метод возвращает набор видимых строк
     * @throws RestrictionEditorException если редактор не смог построить условие выборки
     */
    @Override
    public List<GridRow> getRows() throws RestrictionEditorException {

        /**
         * этот агрегатор используется для фильтрации строк в таблице
         */
        this.restrictionEditor = new RestrictionEditorJPACriteria(this.forClass, true, em);

        // используем условие фильтрации
        this.filterAggregator.applyRestriction(this.getFilter(), this.restrictionEditor);

        // вычисляем количество найденных строк
        CriteriaQuery<Long> nRowsQuery = this.restrictionEditor.getValueCount();
        applyAdditionalConstraints(nRowsQuery);
        Long result = em.createQuery(nRowsQuery).getSingleResult();
        this.pager.setRowsFound(result.intValue());

        this.restrictionEditor = new RestrictionEditorJPACriteria(this.forClass, false, em);
        // используем условие фильтрации
        this.filterAggregator.applyRestriction(this.getFilter(), this.restrictionEditor);
        CriteriaQuery<T> query = this.restrictionEditor.getValue();
        
        // здесь можно добавить дополнительное условие сортировки
        applyAdditionalConstraints(query);

        //  используем условие сортировки
        for (GridFieldSort fs : this.fieldSortList) {
            switch (fs.getSortType()) {
                case ASC:
                    query.orderBy(cb.asc(restrictionEditor.getRoot().get(fs.getAttributeName())));
                    break;
                case DESC:
                    query.orderBy(cb.desc(restrictionEditor.getRoot().get(fs.getAttributeName())));
                    break;
            }
        }

        // Выбираем страницу с заданным номером
        TypedQuery<?> tQuery=em.createQuery(query);
        tQuery.setFirstResult(this.pager.getVisiblePage().getFirstRow()).setMaxResults(this.pager.getRowsPerPage());

        // выбираем строки из БД
        List foundRows = tQuery.getResultList();

        // сюда сохраним строки
        ArrayList<GridRow> rows = new ArrayList<GridRow>();

        // Временный массив
        Map<String, Object> data = new HashMap<String, Object>();

        // список всех колонок
        List<String> rowPropertyNames = this.сlassPropertyAdapter.getPropertyNames();
        // Заполняем список строк

        // для каждого объекта, возвращенного из БД достаём все поля
        // скрытые поля тоже нужны
        for (Object row : foundRows) {
            for (String pn : rowPropertyNames) {
                // System.out.println("row "+row+" ==================> "+pn+"=>"+this.сlassPropertyAdapter.get(row, pn));
                data.put(pn, this.сlassPropertyAdapter.get(row, pn));
            }
            rows.add(new GridRowMap(data));
        }
        return rows;
    }

    /**
     * Применяет дополнительные условия выборки строк
     * этот метод следует перекрывать
     * @param criteria 
     */
    protected void applyAdditionalConstraints(CriteriaQuery<?> criteria) {
    }
}
