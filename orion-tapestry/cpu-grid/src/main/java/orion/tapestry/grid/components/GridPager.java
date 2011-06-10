package orion.tapestry.grid.components;

import java.util.List;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import orion.tapestry.grid.lib.paging.Page;
import orion.tapestry.grid.lib.paging.Pager;

/**
 * Component to draw paging links
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


    public List<Page> getPageList(){
        return this.pager.getPageList();
    }

    public int getRowsFound(){
        return this.pager.getRowsFound();
    }

    public boolean getIsCurrentPage(){
        return ( currentPage.getPageNumber()==this.pager.getVisiblePage().getPageNumber() );

    }

    public boolean getCurrentPageIsNull(){
        return currentPage==null;
    }

//    Object onActionFromGotopage(int pageNumber)
//    {
//        this.pager.setVisiblePage(pageNumber);
//        return null;
//    }

}
