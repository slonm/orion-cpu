package orion.cpu.views.tapestry.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.Request;

/**
 * Страница логина.
 * @author sl
 */
@SuppressWarnings("unused")
//@Secure
public class Login {

    @Inject
    @Value("${spring-security.check.url}")
    private String checkUrl;
    @Inject
    @Value("${spring-security.target.url}")
    private String defaultRedirectUrl;
    @Persist(PersistenceConstants.FLASH)
    @Property
    private String redirectUrl;
    @Inject
    private Request request;

    /**
     * Returns the Spring Security check URL.
     *
     * @return a {@link String}.
     */
    public String getLoginCheckUrl() {
        return request.getContextPath() + checkUrl;
    }

    public void onActivate() {
        if (redirectUrl == null) {
            redirectUrl = request.getContextPath() + defaultRedirectUrl;
        }
    }

    public Login setRedirectURL() {
        redirectUrl = request.getPath();
        return this;
    }
}
