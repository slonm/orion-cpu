package orion.cpu.security;

import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.authentication.entity.User;
import java.io.IOException;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;
import static orion.cpu.views.tapestry.utils.CpuTapestryUtils.subSystemNameByMenupath;

/**
 * Фильтр http запросов.
 * Сохраняет в SSO типа Role значение роли, вычисленное на основании знацения параметра
 * запроса menupath и текущего пользователя. 
 * 
 * Роль представляет собой некий аналог ролей Interbase, но в отличие от него
 * переключение между ролями осуществляется уже после входа пользователя в систему
 * @author sl
 */
//TODO Объеденить с UserASORequestFilter
public class RoleSSORequestFilter implements RequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RoleSSORequestFilter.class);
    private final ApplicationStateManager applicationStateManager;
    private final ExtendedAuthorizer authorizer;

    public RoleSSORequestFilter(ApplicationStateManager applicationStateManager,
            ExtendedAuthorizer authorizer) {
        this.applicationStateManager = Defense.notNull(applicationStateManager, "applicationStateManager");
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        Role role = null;
        User user = applicationStateManager.getIfExists(User.class);
        String menupath = request.getParameter("menupath");
        String subSystemName = subSystemNameByMenupath(menupath);
        if (user != null && subSystemName != null) {
            role = user.roleBySubSystemName(subSystemName);
        }
        if (role != null) {
            role = authorizer.storeUserAndRole(user, role);
            applicationStateManager.set(Role.class, role);
        } else {
            //Сделаем "залипание" роли. Если menupath не указан, то оставим роль прежнего запроса
            role = applicationStateManager.getIfExists(Role.class);
            if(role != null&&subSystemName!=null&&!role.getSubSystem().getName().equalsIgnoreCase(subSystemName)){
                role=null;
            }
            authorizer.storeUserAndRole(user, role);
        }
        LOG.debug("Role now: {}", role == null ? "<none>" : role.toString());
        return handler.service(request, response);
    }
}
