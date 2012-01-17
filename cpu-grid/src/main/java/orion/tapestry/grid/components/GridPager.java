package orion.tapestry.grid.components;

import java.util.List;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import orion.tapestry.grid.lib.datasource.DataSource;
import orion.tapestry.grid.lib.paging.Page;
import orion.tapestry.grid.lib.paging.Pager;

/**
 * Компонента, отображающая ссылки
 * для перехода на другие страницы списка
 * @author Gennadiy Dobrovolsky
 */
public class GridPager {

    /**
     * Номер текущей страницы
     */
    @Parameter("1")
    @Property
    private int currentPage;
    /**
     * Количество строк на странице
     */
    @Parameter("25")
    @Property
    private int rowsPerPage;
    /**
     * The source of data for the Grid to display.
     * This will usually be a List or array but can also be an explicit
     * {@link DataSource}. For Lists and object arrays,
     * a DataSource is created automatically as a wrapper
     * around the underlying List.
     */
    @Parameter(name = "source", principal = true, required = true, autoconnect = true)
    private DataSource source;
    /**
     * Источник/приёмник данных формы,
     * источник данных для цикла
     * Цикл объявлен в шаблоне
     */
    private Pager pager;


    /**
     * Временная переменная для цикла
     * Цикл объявлен в шаблоне
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции 
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private Page page;


    /**
     * Этот метод выполняется перед каждой отрисовкой страницы
     */
    Object beginRender(MarkupWriter writer) {
        this.pager = new Pager();
        this.pager.setRowsPerPage(rowsPerPage);
        this.pager.setVisiblePage(currentPage);
        this.pager.setRowsFound(source.getAvailableRows());
        // this.pager.getVisiblePage()
        return null;
    }

    /**
     * Список ссылок на разные страницы списка
     */
    public List<Page> getPageList() {
        return this.pager.getPageList();
    }

    /**
     * Количество найденных строк в списке
     */
    public int getRowsFound() {
        return this.pager.getRowsFound();
    }

    /**
     * Проверяет, является ли страница текущей
     */
    public boolean getIsCurrentPage() {
        return (page.getPageNumber() == this.pager.getVisiblePage().getPageNumber());

    }

    /**
     * Задана ли текущая страница
     */
    public boolean getCurrentPageIsNull() {
        return page == null;
    }

    /**
     * Возврат текущей страницы
     */
    public Page getVisiblePage(){
        return this.pager.getVisiblePage();
    }
}
