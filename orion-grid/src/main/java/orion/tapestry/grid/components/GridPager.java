package orion.tapestry.grid.components;

import java.util.List;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import orion.tapestry.grid.lib.paging.Page;
import orion.tapestry.grid.lib.paging.Pager;

/**
 * Компонента, отображающая ссылки
 * для перехода на другие страницы списка
 * @author Gennadiy Dobrovolsky
 */
public class GridPager {

    /**
     * Источник/приёмник данных формы,
     * источник данных для цикла
     * Цикл объявлен в шаблоне
     */
    @Parameter(principal = true)
    private Pager pager;
    /**
     * Временная переменная для цикла
     * Цикл объявлен в шаблоне
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции 
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private Page currentPage;

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
        return (currentPage.getPageNumber() == this.pager.getVisiblePage().getPageNumber());

    }

    /**
     * Задана ли текущая страница
     */
    public boolean getCurrentPageIsNull() {
        return currentPage == null;
    }
}
