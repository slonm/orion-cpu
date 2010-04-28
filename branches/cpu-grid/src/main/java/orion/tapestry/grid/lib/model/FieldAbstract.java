/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.tapestry.grid.lib.model;

/**
 * Предок всех типов колонок
 * @param <T> 
 * @author Gennadiy Dobrovolsky
 */
public abstract class FieldAbstract<T> implements FieldInterface<T>{

    private String fieldId;
    private String label;
    private SortingModelEntry sortingModelEntry;
    private boolean isSortable;

    @Override
    public abstract Class<T> getDataType();

    public FieldAbstract(String newFieldId, String newLabel,boolean newIsSortable) {
        this.fieldId = newFieldId;
        this.label = newLabel;
        this.isSortable=newIsSortable;
    }


    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public FieldInterface<T> setLabel(String newLabel) {
        this.label = newLabel;
        return this;
    }

    @Override
    public String getId() {
        return this.fieldId;
    }

    @Override
    public FieldInterface<T> setId(String val) {
        this.fieldId = val;
        return this;
    }

    @Override
    public FieldInterface<T> setSorting(SortingModelEntry newSortingModelEntry) {
        this.sortingModelEntry = newSortingModelEntry;
        return this;
    }

    @Override
    public SortingModelEntry getSorting() {
        return this.sortingModelEntry;
    }

    @Override
    public boolean isSortable() {
        return this.isSortable;
    }

    @Override
    public FieldInterface<T> setSortable(boolean val) {
        this.isSortable = val;
        return this;
    }
    // ================== getters & setters - end ==============================

}
