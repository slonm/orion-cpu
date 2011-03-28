package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска по логину
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByLoginEv<V> extends AbstractAfterFindByEv<V> {

    private final String login;

    public AfterFindByLoginEv(V returnValue, String name) {
        super(returnValue);
        this.login=name;
    }

    public String getLogin() {
        return login;
    }
    
}
