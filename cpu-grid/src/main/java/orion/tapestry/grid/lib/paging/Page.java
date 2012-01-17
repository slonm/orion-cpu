package orion.tapestry.grid.lib.paging;

/**
 * Свойства одной страницы, используются для её отображения
 * @author Gennadiy Dobrovolsky
 */
public class Page {

    /**
     * Page number
     */
    private int pageNumber;

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int _pageNumber) {
        this.pageNumber = _pageNumber;
    }
    /**
     * Number of first row on the page
     */
    private int firstRow;

    /**
     * Number of first row on the page
     */
    public int getFirstRow() {
        return this.firstRow;
    }
    /**
     * Number of last row on the page
     */
    private int lastRow;

    /**
     * Number of last row on the page
     */
    public int getLastRow() {
        return this.lastRow;
    }

    /**
     * @param _pageNumber number of the page (1,2,3 etc.)
     * @param _rowsPerPage rows per page in the list
     * @param _maxRow maximal allowed row number
     */
    public Page(int _pageNumber, int _rowsPerPage, int _maxRow) {
        this.pageNumber = _pageNumber;
        this.firstRow = (_pageNumber - 1) * _rowsPerPage;
        this.lastRow = this.firstRow + _rowsPerPage - 1;
        setMaxRowNumber(_maxRow);
    }

    public Page(int _pageNumber, int _rowsPerPage) {
        this.pageNumber = _pageNumber;
        this.firstRow = (_pageNumber - 1) * _rowsPerPage;
        this.lastRow = this.firstRow + _rowsPerPage - 1;
    }

    public void setMaxRowNumber(int maxRowNumber) {
        if (this.lastRow > maxRowNumber) {
            this.lastRow = maxRowNumber;
        }
    }
}
