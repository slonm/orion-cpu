package orion.cpu.views.tapestry.components;

import br.com.arsmachina.authentication.entity.User;
import java.io.IOException;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.Session;

/**
 * Layout component for pages of application mavenproject1.
 */
@Import(stylesheet={"classpath:orion/cpu/views/tapestry/components/layout/layout.css","../web/cpu_web.css"},
library={"../web/jquery.js","../web/cpu_effects.js"})
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
    @Inject
    private Response response;

    Object defaultMenudata() {
        return "Start";
    }

    /**
     * Invalidates the session.
     */
    @OnEvent(component = "logout", value = EventConstants.ACTION)
    public Object logout() throws IOException {
        final Session session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath());
        return false;
    }
}
