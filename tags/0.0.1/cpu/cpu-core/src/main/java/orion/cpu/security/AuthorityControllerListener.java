package orion.cpu.security;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.controller.Controller;
import orion.cpu.controllers.event.*;
import orion.cpu.controllers.event.base.*;
import orion.cpu.controllers.listenersupport.ControllerEventsListener;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Слушатель событий контроллера для проверки прав на выполнение операций
 * @author sl
 */
public class AuthorityControllerListener implements ControllerEventsListener {

    private final ExtendedAuthorizer authorizer;

    public AuthorityControllerListener(ExtendedAuthorizer authorizer) {
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @Override
    public <T> void onControllerEvent(Controller<T, ?> controller, Class<T> entityClass,
            ControllerEvent event) {
        //Проверяем только before события
        if (!(event instanceof ControllerAfterEvent)) {
            authorizer.check(new Permission(entityClass, event.getOperationType()));
        }
    }
}
