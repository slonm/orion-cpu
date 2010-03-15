package orion.cpu.security;

import br.com.arsmachina.authentication.controller.RoleController;
import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.authentication.entity.User;
import java.io.IOException;
import org.apache.tapestry5.ioc.annotations.PostInjection;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.LinkCreationHub;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Фильтр http запросов.
 * Сохраняет в SSO типа Role значение роли, пришедшее в параметре
 * запроса role. 
 * Создает слушателя для закладывания текущей роли в параметры http запроса.
 * Роль представляет собой некий аналог ролей Interbase, но в отличие от него
 * переключение между ролями осуществляется уже после входа пользователя в систему
 * @author sl
 */
public class RoleSSORequestFilter implements RequestFilter {

    private final ApplicationStateManager applicationStateManager;
    private final RoleController roleController;
    private final ExtendedAuthorizer authorizer;

    public RoleSSORequestFilter(RoleController roleController,
            ApplicationStateManager applicationStateManager, ExtendedAuthorizer authorizer) {
        this.roleController = Defense.notNull(roleController, "roleController");
        this.applicationStateManager = Defense.notNull(applicationStateManager, "applicationStateManager");
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @PostInjection
    public void registerAsListener(LinkCreationHub hub) {
        hub.addListener(new RoleLinkCreationListener(applicationStateManager));
    }

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        Role role = null;
        String roleName = request.getParameter("role");
        User user = applicationStateManager.getIfExists(User.class);
        if (roleName != null && !roleName.isEmpty()) {
            role = roleController.findByLogin(roleName);
        }
        role = authorizer.storeUserAndRole(user, role);
        applicationStateManager.set(Role.class, role);
        return handler.service(request, response);
    }
}
