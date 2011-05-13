package ua.orion.web.services;

import java.io.IOException;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.orion.cpu.core.security.services.ThreadRole;

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
public class ThreadLocalRoleRequestFilter implements RequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalRoleRequestFilter.class);
    private final ThreadRole threadRole;

    public ThreadLocalRoleRequestFilter(ThreadRole threadRole) {
        this.threadRole = threadRole;
    }
    
    @Override
    public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
        String menupath = request.getParameter("menupath");
        String role = roleByMenupath(menupath);
        threadRole.setRole(role);
        LOG.debug("Role now: {}", role == null ? "<none>" : role.toString());
        return handler.service(request, response);
    }
    
    public static String roleByMenupath(String menupath) {
        int startLength = "Start>".length();
        if (menupath == null || menupath.length() <= startLength) {
            return null;
        }
        String subKey = menupath.substring(startLength);
        int gtpos = subKey.indexOf(">");
        if (gtpos > 0) {
            subKey = subKey.substring(0, subKey.indexOf(">"));
        }
        return subKey;
    }
}
