package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.*;

/**
 * Событие после проверки есть ли пользователь с указанным логином
 * @param <V> тип возвращаемого значения метода
 * @author sl
 */
public class AfterHasWithLoginEv extends AbstractAfterViewEv<Boolean> {

    private final String login;

    public AfterHasWithLoginEv(Boolean returnValue, String name) {
        super(returnValue);
        this.login=name;
    }

    public String getLogin() {
        return login;
    }
    
}
