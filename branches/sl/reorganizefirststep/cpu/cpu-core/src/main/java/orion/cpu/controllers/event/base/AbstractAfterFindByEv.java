package orion.cpu.controllers.event.base;

/**
 * Базовый класс событий после поиска по условию
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public abstract class AbstractAfterFindByEv<T> extends AbstractAfterViewEv<T> {

    public AbstractAfterFindByEv(T returnValue) {
        super(returnValue);
    }
}
