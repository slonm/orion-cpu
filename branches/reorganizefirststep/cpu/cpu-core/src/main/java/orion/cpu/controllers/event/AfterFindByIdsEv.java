package orion.cpu.controllers.event;

import java.io.Serializable;
import orion.cpu.controllers.event.base.AbstractAfterViewEv;

/**
 * Событие после поиска по колекции Id
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByIdsEv<T> extends AbstractAfterViewEv<T> {

    private final Serializable[] ids;

    public AfterFindByIdsEv(T returnValue, Serializable[] ids) {
        super(returnValue);
        this.ids = ids;
    }

    public Serializable getId() {
        return ids;
    }

}
