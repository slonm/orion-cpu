package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractAfterViewEv;

/**
 * Событие после извлечения всех записей
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindAllEv<T> extends AbstractAfterViewEv<T> {

    public AfterFindAllEv(T returnValue) {
        super(returnValue);
    }
}
