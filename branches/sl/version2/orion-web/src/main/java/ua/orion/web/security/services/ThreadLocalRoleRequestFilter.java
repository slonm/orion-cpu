package ua.orion.web.security.services;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.orion.cpu.core.security.services.ThreadRole;

/**
 * Фильтр http запросов.
 * Сохраняет в ThreadRole значение роли, вычисленное на основании значения параметра
 * запроса menupath и текущего пользователя. 
 * @author sl
 */
public class ThreadLocalRoleRequestFilter implements HttpServletRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalRoleRequestFilter.class);
    private final ThreadRole threadRole;

    public ThreadLocalRoleRequestFilter(ThreadRole threadRole) {
        this.threadRole = threadRole;
    }

    @Override
    public boolean service(HttpServletRequest request,
            HttpServletResponse response, HttpServletRequestHandler handler) throws IOException {
        String role = request.getParameter("role");
        threadRole.setRole(role);
        LOG.debug("Role now: {}", role == null ? "<none>" : role.toString());
        return handler.service(request, response);
    }
}
