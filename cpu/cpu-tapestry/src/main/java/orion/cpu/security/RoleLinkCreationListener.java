package orion.cpu.security;

import br.com.arsmachina.authentication.entity.Role;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.LinkCreationListener;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Слушатель создания ссылок.
 * Закладывает в ссылку параметр role с именем SSO роли.
 * Роль представляет собой некий аналог ролей Interbase, но в отличие от него
 * переключение между ролями осуществляется уже после входа пользователя в систему
 * @author sl
 */
public class RoleLinkCreationListener implements LinkCreationListener {

    private final ApplicationStateManager applicationStateManager;

    public RoleLinkCreationListener(ApplicationStateManager applicationStateManager) {
        this.applicationStateManager = Defense.notNull(applicationStateManager, "applicationStateManager");
    }

    private void createdRenderLink(Link link) {
        Role role = applicationStateManager.getIfExists(Role.class);
        if (role != null && link.getParameterValue("role") == null) {
            link.addParameter("role", role.getLogin());
        }
    }

    @Override
    public void createdPageRenderLink(Link link) {
        createdRenderLink(link);
    }

    @Override
    public void createdComponentEventLink(Link link) {
        createdRenderLink(link);
    }
}
