package orion.cpu.views.tapestry.components;

import br.com.arsmachina.authentication.entity.User;
import java.util.ArrayList;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.services.CpuMenu;

/**
 * Layout component for pages of application mavenproject1.
 */
@IncludeStylesheet({"classpath:orion/cpu/views/tapestry/components/layout/layout.css"})
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
    @Parameter(required = true)
    @Property(write = false)
    private Object menudata;
    @SessionState(create = false)
    @Property
    private User user;
    @Inject
    private Request request;

    Object defaultMenudata() {
        return "Start";
    }

    /**
     * Invalidates the session.
     */
    @OnEvent(component = "logout", value = EventConstants.ACTION)
    public void logout() {
        final Session session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
