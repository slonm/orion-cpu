package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска по символическому ключу справочника
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByKeyEv<V> extends AbstractAfterFindByEv<V> {

    private final String name;

    public AfterFindByKeyEv(V returnValue, String name) {
        super(returnValue);
        this.name=name;
    }

    public String getName() {
        return name;
    }
    
}
