package orion.tapestry.grid.lib.model;

/**
 * SortingModelEntry
 * @author Gennadiy Dobrovolsky
 */
public class SortingModelEntry implements Comparable<SortingModelEntry> {

    /**
     * Вес условия в правилах упорядочения
     * Условия с большим весом используются раньше:
     * если в запросе SQL стоит выражение
     * ORDER BY lastname asc, age desc
     * то поле lastname имеет weight больший, чем поле age
     */
    public int weight = 0;
    /**
     * Идентификатор поля, по которому надо сортировать
     */
    public String fieldId;
    /**
     * Тип сортировки, может принимать значение
     * SortingModelEntry.ASC (сортировка по возрастанию)
     * или SortingModelEntry.DESC (сортировка по убыванию)
     */
    public int type = 0;
    /**
     * Константа, одно из возможных значений параметра type.
     * Означает сортировку по возрастанию
     */
    public static final short ASC = 0;
    /**
     * Константа, одно из возможных значений параметра type.
     * Означает сортировку по убыванию
     */
    public static final short DESC = 1;

    /**
     * Объект,который полностью описывает сортируемое поле
     */
    public FieldInterface<?> field;

    /**
     * Конструктор
     * @param _fieldId строковый идентификатор поля
     * @param _weight  вес поля, определяющий порядок сортировки
     * @param _type    тип сортировки (по возрастанию или по убыванию)
     */
    public SortingModelEntry(String _fieldId, int _type, int _weight) {
        this.fieldId = _fieldId;
        this.type = (_type == SortingModelEntry.ASC) ? SortingModelEntry.ASC : SortingModelEntry.DESC;
        this.weight = _weight;
    }

    /**
     * Используется для упорядочения списка полей для сортировки
     */
    @Override
    public int compareTo(SortingModelEntry o) {
        return o.weight - this.weight;
    }

    /**
     * Используется для извлечения из коллекции и удаления из неё
     * @param o объект, с которым сравниваем
     */
    @Override
    public boolean equals(Object o) {
        if (!o.getClass().isInstance(SortingModelEntry.class)) {
            return false;
        }
        return this.fieldId.equalsIgnoreCase(((SortingModelEntry) o).fieldId);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.fieldId != null ? this.fieldId.hashCode() : 0);
        return hash;
    }
}
