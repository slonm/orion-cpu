
package orion.tapestry.grid.lib.model;

/**
 * Интерфейс фильтра для передачи в источник данных
 * @param <T> тип данных для которого предназначен фильтр
 * @param <X> поле в таблице, которое хранит данные типа T
 * @author Gennadiy Dobrovolsky
 */
public interface IFilter<T, X extends FieldInterface<T>> {
    // надо передавать данные фильтра в источник данных
}
