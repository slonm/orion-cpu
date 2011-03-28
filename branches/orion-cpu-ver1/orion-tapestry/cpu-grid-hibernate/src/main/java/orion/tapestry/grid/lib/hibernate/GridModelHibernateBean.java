package orion.tapestry.grid.lib.hibernate;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.Messages;
import orion.tapestry.grid.lib.field.GridFieldFactory;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.filter.FilterAggregator;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.model.GridModelAdapter;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorHumanReadable;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.lib.rows.impl.GridRowMap;

/**
 * Конструирует модель из HibernateBean
 * @author Gennadiy Dobrovolsky
 */
public class GridModelHibernateBean extends GridModelAdapter<Criteria> {

    /**
     * Класс сущности
     */
    private Class forClass;
    /**
     * Конфигурация, отображение стандартных типов Java в типы полей
     */
    private Map<String, Class> configuration;

    /**
     * Сессия Hibernate, подключание к базе данных
     */
    private Session session;
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

        // вычисляем количество найденных строк
        {
            this.filterAggregator.applyRestriction(this.getFilter(), this.restrictionEditor);
            Criteria nRowsQuery = (Criteria) this.restrictionEditor.getValue();
            applyAdditionalConstraints(nRowsQuery);
            nRowsQuery.setProjection(Projections.rowCount());
            //System.out.println("--------2--------");
            Long result = (Long) nRowsQuery.uniqueResult();
            this.pager.setRowsFound(result.intValue());
            //System.out.println("--------3--------");
            //System.out.println(result+" rows");
        }

        // используем условие фильтрации
        this.filterAggregator.applyRestriction(this.getFilter(), this.restrictionEditor);
        Criteria query = (Criteria) this.restrictionEditor.getValue();
        // System.out.println(query.toString());
        // List foundRows1 = query.list();
        // System.out.println(foundRows1.size() + " rows extracted");

        // здесь можно добавить дополнительное условие сортировки
        applyAdditionalConstraints(query);

        //  используем условие сортировки
        for (GridFieldSort fs : this.fieldSortList) {
            //System.out.println(fs.getLabel()+"  "+fs.getOrdering());
            switch (fs.getSortType()) {
                case ASC:
                    query.addOrder(Order.asc(fs.getAttributeName()));
                    break;
                case DESC:
                    query.addOrder(Order.desc(fs.getAttributeName()));
                    break;
            }
        }

        // Выбираем страницу с заданным номером
        query.setFirstResult(this.pager.getVisiblePage().getFirstRow()).setMaxResults(this.pager.getRowsPerPage());

        // выбираем строки из БД
        List foundRows = query.list();

        // сюда сохраним строки
        ArrayList<GridRow> rows = new ArrayList<GridRow>();

        // Временный массив
        Map<String, Object> data = new HashMap<String, Object>();

        // Заполняем список строк

        // для каждого объекта, возвращенного из БД ...
        for (Object i : foundRows) {

            // для каждого поля
            for (GridFieldAbstract key : this.getFields()) {

                // если поле соответствует атрибуту ...
                if (key.getAttributeName() != null) {
                    data.put(key.getUid(), this.сlassPropertyAdapter.get(i, key.getUid()));
                }
            }
            rows.add(new GridRowMap(data));
        }
        return rows;
    }

    /**
     * Конструктор составляет список полей таблицы
     * и задаёт их начальные свойства
     * @param _forClass класс-сущность для Hibernate
     * @param _configuration конфигурация, соответствие тип атрибута &lt;=&gt; тип колонки в таблице
     * @param _messages сообщения для интерфейса
     * @param _session Hibernate session object
     * @throws IntrospectionException происходит, если классу GridFieldFactory не удалось понять структуру класса forClass
     */
    public GridModelHibernateBean(
            Class _forClass,
            Map<String, Class> _configuration,
            Messages _messages,
            Session _session) throws IntrospectionException {

        this.forClass = _forClass;
        this.configuration = _configuration;
        this.messages = _messages;
        this.session = _session;

        // =========== объявляем поля таблицы - начало =========================
        // поля надо объявлять обязательно, а то в таблице не будет колонок
        this.fieldList = GridFieldFactory.getFields(forClass, configuration, messages);
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
        this.restrictionEditor = new RestrictionEditorHibernateCriteria(this.forClass, this.session);

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
        this.сlassPropertyAdapter = GridFieldFactory.getClassPropertyAdapter(forClass);
    }

    /**
     * Применяет дополнительные условия выборки строк
     * этот метод следует перекрывать
     * @param criteria 
     */
    protected void applyAdditionalConstraints(Criteria criteria) {
    }
}
