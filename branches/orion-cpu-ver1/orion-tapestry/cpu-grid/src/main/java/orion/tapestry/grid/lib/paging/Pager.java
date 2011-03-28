package orion.tapestry.grid.lib.paging;

import java.util.ArrayList;
import java.util.List;

/**
 * Данные для постраничного показа списка
 * @author Gennadiy Dobrovolsky
 */
public class Pager {
    /**
     * Number of visible page
     */
    private Page visiblePage;
    /**
     * Rows per page
     */
    private int rowsPerPage;
    /**
     * Total number of rows in the list
     */
    private int rowsFound;

    public Page getVisiblePage() {
        return this.visiblePage;
    }

    public void setVisiblePage(Page _visiblePage) {
        this.visiblePage = _visiblePage;
    }

    public void setVisiblePage(int _visiblePageNumber) {
        this.visiblePage = new Page(_visiblePageNumber, this.rowsPerPage, this.rowsFound);
    }

    public int getRowsPerPage() {
        return this.rowsPerPage;
    }

    public void setRowsPerPage(int _rowsPerPage) {
        this.rowsPerPage = _rowsPerPage;
    }

    public int getRowsFound() {
        return this.rowsFound;
    }

    public void setRowsFound(int _rowsFound) {
        this.rowsFound = _rowsFound;
    }

    /**
     * Составляет список номеров страниц для показа.
     * Чтобы не загромождать интерфейс, следует показывать до 20 ссылок
     * с номерами страниц для перехода на соответствующую страницу списка.
     * Переход на 1-ю и последнюю страницы есть всегда.
     * Остальные номера страниц - это страницы, ближайшие к текущей.
     * @return list of pages used to show page links
     */
    public List<Page> getPageList() {
        ArrayList<Page> pl = new ArrayList<Page>();

        long imin = Math.max(0, this.visiblePage.getFirstRow() - 5 * this.rowsPerPage);
        long imax = Math.min(this.rowsFound, this.visiblePage.getFirstRow() + 5 * this.rowsPerPage);

        if (imin > 0) {
            pl.add(new Page(1, this.rowsPerPage, this.rowsFound));
            // если нумерация страниц идёт не подряд, ставим NULL вместо пропущенных номеров
            if (imin > this.rowsPerPage) {
                pl.add(null);
            }
        }
        for (long iPage = imin; iPage < imax; iPage += this.rowsPerPage) {
            pl.add(new Page(Math.round(1 + iPage / this.rowsPerPage), this.rowsPerPage, this.rowsFound));
        }
        if (imax < this.rowsFound) {
            int lastPageNumber = 1 + (int) Math.floor((this.rowsFound - 1) / this.rowsPerPage);

            // если нумерация страниц идёт не подряд, ставим NULL вместо пропущенных номеров
            if ((pl.get(pl.size() - 1).getPageNumber() + 1) != lastPageNumber) {
                pl.add(null);
            }
            pl.add(new Page(lastPageNumber, this.rowsPerPage, this.rowsFound));
        }
        return pl;
    }

    public static void main(String[] args) {
        Pager pgr = new Pager();
        pgr.setRowsFound(555);
        pgr.setRowsPerPage(10);
        pgr.setVisiblePage(15);
        for (Page p : pgr.getPageList()) {
            System.out.println("Page " + p.getPageNumber() + ": from " + p.getFirstRow() + " to " + p.getLastRow());
        }
    }
}
