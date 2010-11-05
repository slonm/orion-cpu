package orion.cpu.security;

import java.io.IOException;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.RequestFilter;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.authentication.entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link RequestFilter} that sets the {@link User} application state object if Spring Security has
 * an authenticated user in a session.
 * Если аутентификация не выполнена, то устанавливается пользователь GUEST и у него не изменяется флаг isLoggedIn
 * @author sl
 */
public class AuthorizerUserAndRoleRequestFilter implements HttpServletRequestFilter {

    private final ExtendedAuthorizer authorizer;

    /**
     * Single constructor of this class.
     *
     * @param userController an {@link UserController}. It cannot be null.
     * @param applicationStateManager an {@link ApplicationStateManager}. It cannot be null.
     */
    public AuthorizerUserAndRoleRequestFilter(
            ExtendedAuthorizer authorizer) {
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @Override
    public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("sso:" + User.class.getName());
            if (user != null) {
                Role role = (Role) session.getAttribute("sso:" + Role.class.getName());
                authorizer.storeUserAndRole(user, role);
            }
        }
        return handler.service(request, response);
    }
}
