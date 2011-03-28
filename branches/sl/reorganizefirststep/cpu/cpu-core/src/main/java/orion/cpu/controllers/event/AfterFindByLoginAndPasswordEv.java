package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после поиска по логину и паролю
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterFindByLoginAndPasswordEv<V> extends AbstractAfterFindByEv<V> {

    private final String login;
    private final String password;

    public AfterFindByLoginAndPasswordEv(V returnValue, String login, String password) {
        super(returnValue);
        this.login=login;
        this.password=password;
    }

    public String getLogin() {
        return login;
    }
    
    public String getPassword() {
        return password;
    }

}
