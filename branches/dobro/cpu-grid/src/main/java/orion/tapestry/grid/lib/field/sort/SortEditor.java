package orion.tapestry.grid.lib.field.sort;

/**
 * Редактор выражения, которое управляет упорядочением строк
 * @param <T> тип выражения, описывающего упорядочение строк - результата рабоы редактора
 * @author Gennadiy Dobrovolsky
 */
public interface SortEditor<T> {
    
    /**
     * Создаёт новое выражение и удаляет старое
     */
    public void createNew();

    /**
     * Добавляет ещё одно правило сортировки
     * @param fs объект, описывающий сортировку по одному полю
     */
    public void addFieldSort(GridFieldSort fs);

    /**
     * Удаляет правило сортировки с заданным UID
     * @param uid идентификатор правила сортировки, которое надо удалить
     */
    public void delFieldSort(String uid);

    /**
     * Вычисляет окончательное выражение, единое правило сортировки
     * @return правило сортировки, которое уже можно передавать в извлекатель данных
     */
    public T getValue();
}
