package orion.cpu.security;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.User;
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

//TODO Row Level Security, Column Level Security
    @Override
    public <T> void onControllerEvent(Controller<T, ?> controller, Class<T> entityClass,
            ControllerEvent event) {
        //Проверяем только before события
        if (!(event instanceof ControllerAfterEvent)) {
            //Разрешим всем делать немодифицирующие операции с таблицами системы безопасности
            if (!(event instanceof ModifyEv)) {
                if (controller instanceof PermissionController ||
                        controller instanceof PermissionGroupController ||
                        controller instanceof RoleController ||
                        controller instanceof UserController) {
                    return;
                }
                //Разрешим переключать флаги LoggedIn, Locked у пользователя
                //FIXME сейчас полностью разрешен update пользователя
            } else if (controller instanceof UserController && event instanceof BeforeUpdateEv) {
                return;
            }

            authorizer.check(new Permission(entityClass, event.getOperationType()));
        }
    }
}
