package ua.orion.web.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tapestry5.AbstractOptionModel;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.util.AbstractSelectModel;
import ua.orion.cpu.core.security.entities.ActiveDirectoryPrincipal;
import ua.orion.web.services.LastPageHolder;

/**
 * Layout component for pages.
 */
@Import(stylesheet = {"layout/layout.css"})
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
    private String role;
    @Inject
    private Request request;
    @Inject
    private Response response;
    @Inject
    private ComponentResources resources;
    @Inject
    private LastPageHolder holder;

    public Object getMenudata() {
        return defaultMenudata();
    }

    Object defaultMenudata() {
        return request.getParameter("menupath") == null ? "Start" : request.getParameter("menupath");
    }

    public String getRole() {
        return request.getParameter("role");
    }

    public void setRole(String role) {
        this.role = role;
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

    public Object onSetRoleMode() {
        Link link = holder.getLastPage();
        link.removeParameter("role");
        return link;
    }

    public SelectModel getRoleModel() {
        return new AbstractSelectModel() {

            @Override
            public List<OptionGroupModel> getOptionGroups() {
                return null;
            }

            @Override
            public List<OptionModel> getOptions() {
                List<OptionModel> optionModelList = new ArrayList<OptionModel>();
                PrincipalCollection pc = SecurityUtils.getSubject().getPrincipals();

                if (pc != null) {
                    ActiveDirectoryPrincipal adp = pc.oneByType(ActiveDirectoryPrincipal.class);
                    if (adp != null) {
                        Set<String> roles = adp.getRoles();
                        for (final String _role : roles) {
                            optionModelList.add(new AbstractOptionModel() {

                                @Override
                                public String getLabel() {
                                    return _role;
                                }

                                @Override
                                public Object getValue() {
                                    return _role;
                                }
                            });
                        }
                    }
                }
                return optionModelList;
            }
        };
    }

    public Object onActionFromRoleForm() {
        Link link = holder.getLastPage();
        link.addParameter("role", role);
        return link;
    }
}
