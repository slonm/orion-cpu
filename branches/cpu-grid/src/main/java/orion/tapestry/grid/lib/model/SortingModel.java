package orion.tapestry.grid.lib.model;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Описывает способ сортировки в отображаемой таблице
 * @author Gennadiy Dobrovolsky
 */
public class SortingModel {

    private TreeSet<SortingModelEntry> fieldSorting;

    /**
     * Создаёт пустой список 
     */
    public SortingModel() {
        fieldSorting = new TreeSet<SortingModelEntry>();
    }

    /**
     * Добавляет новые элементы в множество для сортировки
     * @param newSortingModelEntry элементы, которые будем добавлять
     * @return ссылка на текущий объект для организации цепочки
     */
    public SortingModel add(SortingModelEntry... newSortingModelEntry) {
        for (SortingModelEntry it : newSortingModelEntry) {
            fieldSorting.add(it);
        }
        return this;
    }

    /**
     * Удаляем элементы
     * @param rmSortingModelEntry список элементов для удаления
     * @return ссылка на текущий объект для организации цепочки
     */
    public SortingModel remove(SortingModelEntry... rmSortingModelEntry) {
        for (SortingModelEntry it : rmSortingModelEntry) {
            fieldSorting.remove(it);
        }
        return this;
    }

    /**
     * Удаляем элементы
     * @param rmSortingModelEntry имена элементов для удаления
     * @return ссылка на текущий объект для организации цепочки
     */
    public SortingModel remove(String... rmSortingModelEntry) {
        for (String it : rmSortingModelEntry) {
            fieldSorting.remove(getSortingModelEntry(it));
        }
        return this;
    }

    /**
     * @param fieldId имя поля, для которого надо найти правило сортировки
     * @return  sorting rule by fieldId
     */
    public SortingModelEntry getSortingModelEntry(String fieldId) {
        for (SortingModelEntry it : this.fieldSorting) {
            if (it.fieldId.equalsIgnoreCase(fieldId)) {
                return it;
            }
        }
        return null;
    }

    /**
     * @return  ordered sorting rules
     */
    public TreeSet<SortingModelEntry> getSortingModelEntry() {
        return this.fieldSorting;
    }

    /**
     * @param availableFields список всех полей,
     * по которым можно сортировать записи в таблице
     * @return список полей, по которым наод сортировать
     */
    public ArrayList<FieldInterface<?>> getSorting(ArrayList<FieldInterface<?>> availableFields) {
        ArrayList<FieldInterface<?>> sorting = new ArrayList<FieldInterface<?>>();
        for (SortingModelEntry it : this.fieldSorting) {
            for (FieldInterface<?> fld : availableFields) {
                if (!fld.isSortable()) {
                    continue;
                }
                if (fld.getId().equalsIgnoreCase(it.fieldId)) {
                    sorting.add(fld);
                    break;
                }
            }
        }
        return sorting;
    }
}
