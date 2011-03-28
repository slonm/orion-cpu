package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после загрузки всего
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterLoadEverythingEv<V> extends AbstractAfterViewEv<V> {

    private final String login;

    public AfterLoadEverythingEv(V returnValue, String name) {
        super(returnValue);
        this.login=name;
    }

    public String getLogin() {
        return login;
    }
    
}
