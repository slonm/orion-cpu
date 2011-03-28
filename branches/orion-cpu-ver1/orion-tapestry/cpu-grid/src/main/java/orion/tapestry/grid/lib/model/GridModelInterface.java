package orion.tapestry.grid.lib.model;

import java.util.List;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.paging.Pager;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.view.GridFieldView;
import orion.tapestry.grid.lib.field.filter.FilterElementAbstract;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.rows.GridRow;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public interface GridModelInterface {

    /**
     * Возвращает список полей.
     * Каждое поле отображается как колонка в таблице.
     * Поле может быть невидимым.
     * Каждое поле - это набор свойств, которые управляют отображением таблицы:
     * название поля, UID поля, настройки отображения, сортировки, фильтрации
     * @return список полей в таблице
     */
    public List<GridFieldAbstract> getFields();

    /**
     * @return данные для постраничного показа
     */
    public Pager getPager();

    /**
     * Получает снаружи новую информацию о разбивке списка на страницы
     * @param _pager новые данные о разбивке на страницы
     */
    public void setPager(Pager _pager);

    /**
     * Возвращает (и при необходоимости создаёт) список объектов GridFieldSort
     * которые хранят информацию о упорядочении строк в таблице
     * @return список объектов, которые содержат информацию о упорядочении строк в таблице
     */
    public List<GridFieldSort> getFieldSortList();

    /**
     * Получает список объектов GridFieldSort
     * которые хранят информацию о упорядочении строк в таблице
     * @param _fieldSort новые объекты с информацией об упорядочении строк
     */
    public void setFieldSortList(List<GridFieldSort> _fieldSort);

    /**
     * Возвращает (и при необходоимости создаёт) список объектов GridFieldView
     * которые хранят информацию о видимости и порядке столбцов в таблице
     * @return список объектов, которые содержат информацию о упорядочении строк в таблице
     */
    public List<GridFieldView> getFieldViewList();

    /**
     * Получает список объектов GridFieldView
     * которые хранят информацию о Информация о видимости и порядке столбцов в таблице
     * @param _fieldView новые объекты с информацией о видимости и порядке столбцов
     */
    public void setFieldViewList(List<GridFieldView> _fieldView);

    /**
     * Возвращает список фильтров
     * @return список фильтров
     */
    public List<FilterElementAbstract> getFilterElementList();

    /**
     * Получить список фильтров
     * @param _fieldFilterList новый список фильтров
     */
    public void setFilterElementList(List<FilterElementAbstract> _fieldFilterList);

    /**
     * Передаём в модель описание фильтра
     * @param node описание фильтра в сериализованом виде
     */
    public void setFilter(String node);

    /**
     * Передаём в модель описание фильтра
     * @param node описание фильтра в сериализованом виде
     */
    public String getFilter();
    /**
     * Основное действие - извлечение строк
     * @return Список строк
     */
    public List<GridRow> getRows() throws RestrictionEditorException;

    /**
     * Добавление поля в модель
     * @param fld поле, которое наддо добавить в модель
     */
    public void addField(GridFieldAbstract fld);

    /**
     * Извлечение поля с заданным идентификатором
     * @param uid поле, которое наддо добавить в модель
     * @return Объект, описывающий заданное поле
     */
    public GridFieldAbstract getField(String uid);

    /**
     * Удаление поля из модели
     * @param uid поле, которое надо удалить из модели
     */
    public void delField(String uid);

    /**
     * Добавление дополнительного фильтра в модель
     * @param flt объект, содержащий дополнительные условия фильтрации
     */
    public void addFilterElement(FilterElementAbstract flt);


    /**
     * Получить читаемую человеком строку, которая описывает фильтры
     * @return Понятное для человека описание условия фильтрации
     */
    public String getHumanReadableFilterInfo();
}
