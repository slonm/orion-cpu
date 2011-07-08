package orion.tapestry.grid.lib.jpa;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import orion.tapestry.grid.lib.field.filter.FilterAggregator;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.model.GridModelAdapter;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorHumanReadable;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.lib.rows.impl.GridRowMap;
import orion.tapestry.grid.services.GridFieldFactory;

/**
 * Конструирует модель из Bean
 * @author Gennadiy Dobrovolsky
 */
public class GridModelJPABean extends GridModelAdapter<CriteriaQuery> {

    /**
     * Класс сущности
     */
    private Class forClass;
    /**
     * Конфигурация, 
     * отображение стандартных типов Java в типы полей
     * и отображение имен атрибутов в типы полей
     */
    private GridFieldFactory gridFieldFactory;
    /**
     * JPA EntityManager, подключание к базе данных
     */
    private EntityManager entityManager;
    /**
     * Адаптер для извлечения полей записи
     */
    private ClassPropertyAdapter сlassPropertyAdapter;

// =============================================================================
// Эти методы надо заменять своими
    /**
     * Метод возвращает набор видимых строк
     * @throws RestrictionEditorException если редактор не смог построить условие выборки
     */
    @Override
    public List<GridRow> getRows() throws RestrictionEditorException {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        Root from;
        // ---------- вычисляем количество найденных строк - begin -------------
        // применяем условия выборки
        this.filterAggregator.applyRestriction(this.getFilter(), this.restrictionEditor);

        // получаем сформированняй запрос
        CriteriaQuery nRowsQuery = this.restrictionEditor.getValue();

        // получаем корень
        from = (Root) nRowsQuery.getRoots().iterator().next();

        // указываем, что будем вычислять коичество строк
        nRowsQuery.select(criteriaBuilder.count(from));

        // применяем дополнительные условия
        applyAdditionalConstraints(nRowsQuery);

        Long result = (Long) this.entityManager.createQuery(nRowsQuery).getSingleResult();
        this.pager.setRowsFound(result.intValue());
        // ---------- вычисляем количество найденных строк - end ---------------

        // ---------- выбираем строки - begin ----------------------------------
        this.restrictionEditor.createEmpty();
        // используем условие фильтрации
        this.filterAggregator.applyRestriction(this.getFilter(), this.restrictionEditor);
        CriteriaQuery query = this.restrictionEditor.getValue();

        // здесь можно добавить дополнительное условие сортировки
        applyAdditionalConstraints(query);

        // получаем корень запроса
        from = (Root) query.getRoots().iterator().next();

        // ---------- используем условие сортировки - begin --------------------
        List ordering = new ArrayList();
        for (GridFieldSort fs : this.fieldSortList) {
            //System.out.println(fs.getLabel()+"  "+fs.getOrdering());
            switch (fs.getSortType()) {
                case ASC:
                    ordering.add(criteriaBuilder.asc(from.get(fs.getAttributeName())));
                    break;
                case DESC:
                    ordering.add(criteriaBuilder.desc(from.get(fs.getAttributeName())));
                    break;
            }
        }
        query.orderBy(ordering);
        // ---------- используем условие сортировки - end ----------------------


        // Выбираем страницу с заданным номером
        TypedQuery typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(this.pager.getVisiblePage().getFirstRow()).setMaxResults(this.pager.getRowsPerPage());

        // выбираем строки из БД
        List foundRows = typedQuery.getResultList();
        // ---------- выбираем строки - end ------------------------------------

        // ---------- Заполняем список строк - begin ---------------------------
        // сюда сохраним строки
        ArrayList<GridRow> rows = new ArrayList<GridRow>();

        // Временный массив
        Map<String, Object> data = new HashMap<String, Object>();

        // список всех колонок
        List<String> rowPropertyNames = this.сlassPropertyAdapter.getPropertyNames();

        // для каждого объекта, возвращенного из БД достаём все поля
        // скрытые поля тоже нужны
        for (Object row : foundRows) {
            for (String pn : rowPropertyNames) {
                data.put(pn, this.сlassPropertyAdapter.get(row, pn));
            }
            rows.add(new GridRowMap(data));
        }
        // ---------- Заполняем список строк - end -----------------------------

        return rows;
    }

    /**
     * Конструктор составляет список полей таблицы
     * и задаёт их начальные свойства
     * @param _forClass класс-сущность
     * @param _configuration конфигурация, соответствие тип атрибута &lt;=&gt; тип колонки в таблице
     * @param _entityManager JPA entityManager object
     * @throws IntrospectionException происходит, если классу GridFieldFactory не удалось понять структуру класса forClass
     */
    public GridModelJPABean(
            Class _forClass,
            GridFieldFactory _gridFieldFactory,
            EntityManager _entityManager) throws IntrospectionException {

        this.forClass = _forClass;
        this.gridFieldFactory = _gridFieldFactory;
        this.entityManager = _entityManager;

        // =========== объявляем поля таблицы - начало =========================
        // поля надо объявлять обязательно, а то в таблице не будет колонок
        this.fieldList = _gridFieldFactory.getFields(forClass);
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
         * этот агрегатор используется для фильтрации строк в таблице
         */
        this.restrictionEditor = new RestrictionEditorJPACriteria(this.forClass, this.entityManager);

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
        //this.сlassPropertyAdapter = gridFieldFactory.getClassPropertyAdapter(forClass);
    }

    /**
     * Применяет дополнительные условия выборки строк
     * этот метод следует перекрывать
     * @param criteria 
     */
    protected void applyAdditionalConstraints(CriteriaQuery criteria) {
    }
}
