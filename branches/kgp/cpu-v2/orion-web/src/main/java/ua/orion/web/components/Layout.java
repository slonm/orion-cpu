package ua.orion.web.components;

import java.io.IOException;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import ua.orion.cpu.core.security.services.ThreadRole;

/**
 * Layout component for pages.
 */
@Import(stylesheet = {"layout/layout.css","../css/cpu_web.css"}, library={"../jquery.js","../cpu_effects.js"})
@SuppressWarnings("unused")
public class Layout {

    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;
    /**
     * Page navigation menu
     */
//    @Parameter(required = true)
//    @Property(write = false)
//    private Object menudata;
    @Inject
    private Request request;
    @Inject
    private Response response;
    @Inject
    private ThreadRole thRole;

    public String getRole() {
        return thRole.getRole();
    }
    
    public Object getMenudata() {
        return defaultMenudata();
    }

    Object defaultMenudata() {
        return request.getParameter("menupath") == null ? "Start" : request.getParameter("menupath");
    }

    public String getUser() {
        //TODO Возвращать ФИО
        return SecurityUtils.getSubject().getPrincipals().oneByType(String.class);
    }

    /**
     * Invalidates the session.
     */
    @OnEvent(component = "logout", value = EventConstants.ACTION)
    public Object logout() throws IOException {
        SecurityUtils.getSubject().logout();
        response.sendRedirect(request.getContextPath());
        return false;
    }

}
