package orion.tapestry.grid.lib.model;

import java.util.Map;

/**
 * Data model used to show grid
 * @author Gennadiy Dobrovolsky
 */
public interface IModel {

    /* ------------------ List of fields - begin ---------------------------  */
    /**
     * @return list of available fields
     */
    public Map<String, FieldInterface<?>> getAvailableFields();

    /**
     * @return list of visible fields
     */
    public Map<String, FieldInterface<?>> getVisibleFields();

    /**
     * @return list of sortable fields
     */
    public Map<String, FieldInterface<?>> getSortableFields();

    /**
     * @return list of searchable fields
     */
    public Map<String, FieldInterface<?>> getSearchableFields();

    /**
     * Add new field to model
     * @param newField the field to add
     * @return link to current object to enable chain configuration
     */
    public IModel addField(FieldInterface<?> newField);

    /**
     * Remove field from model
     * @param fieldId identifier of field
     * @return link to current object to enable chain configuration
     */
    public IModel rmField(String fieldId);

    /**
     * Get field from model
     * @param fieldId identifier of field
     * @return the field object
     */
    public FieldInterface<?> getField(String fieldId);
    /* ------------------ List of fields - end -----------------------------  */



    /* ------------------ Number of rows - begin ---------------------------  */
    /**
     * @return Rows per page in the list
     */
    public int getRowsPerPage();

    /**
     * @param newRowsPerPage new value for "Rows per page"
     * @return link to current object to enable chain configuration
     */
    public IModel setRowsPerPage(int newRowsPerPage);

    /**
     * @return Total rows in the list (including hidden rows)
     */
    public int getRowsTotal();

    /**
     * @return current page number
     */
    public int getCurrentPage();

    /**
     * @param newCurrentPage new number of current page
     * @return link to current object to enable chain configuration
     */
    public IModel setCurrentPage(int newCurrentPage);
    /* ------------------ Number of rows - end -----------------------------  */
}
