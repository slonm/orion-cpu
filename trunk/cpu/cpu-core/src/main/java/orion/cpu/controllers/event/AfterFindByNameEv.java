package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска по имени
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByNameEv<V> extends AbstractAfterFindByEv<V> {

    private final String name;

    public AfterFindByNameEv(V returnValue, String name) {
        super(returnValue);
        this.name=name;
    }

    public String getName() {
        return name;
    }
    
}
