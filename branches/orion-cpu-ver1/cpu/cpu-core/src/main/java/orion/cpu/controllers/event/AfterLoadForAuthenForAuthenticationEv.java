package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после загрузки для аутентификации
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterLoadForAuthenForAuthenticationEv<V> extends AbstractAfterViewEv<V> {

    private final String login;

    public AfterLoadForAuthenForAuthenticationEv(V returnValue, String name) {
        super(returnValue);
        this.login=name;
    }

    public String getLogin() {
        return login;
    }
    
}
