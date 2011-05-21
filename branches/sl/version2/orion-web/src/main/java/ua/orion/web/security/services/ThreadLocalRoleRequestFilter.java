package ua.orion.web.security.services;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.orion.cpu.core.security.services.ThreadRole;

/**
 * Фильтр http запросов.
 * Сохраняет в ThreadRole значение роли, вычисленное на основании значения параметра
 * запроса role. 
 * @author sl
 */
public class ThreadLocalRoleRequestFilter implements HttpServletRequestFilter, PageRenderLinkTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalRoleRequestFilter.class);
    private final ThreadRole threadRole;

    public ThreadLocalRoleRequestFilter(ThreadRole threadRole) {
        this.threadRole = threadRole;
    }

    @Override
    public boolean service(HttpServletRequest request,
            HttpServletResponse response, HttpServletRequestHandler handler) throws IOException {
        String role = request.getParameter("role");
        if ("".equals(role)) {
            role = null;
        }
        if (role != null && !SecurityUtils.getSubject().hasRole(role)) {
            role = null;
        }
        threadRole.setRole(role);
        LOG.debug("Role now: {}", role == null ? "<none>" : role.toString());
        return handler.service(request, response);
    }

    @Override
    public Link transformPageRenderLink(Link defaultLink, PageRenderRequestParameters parameters) {
        String role = threadRole.getRole();
        if (!defaultLink.getParameterNames().contains("role") && role != null) {
            defaultLink.addParameter("role", role);
        }
        return null;
    }

    @Override
    public PageRenderRequestParameters decodePageRenderRequest(Request request) {
        return null;
    }
}
