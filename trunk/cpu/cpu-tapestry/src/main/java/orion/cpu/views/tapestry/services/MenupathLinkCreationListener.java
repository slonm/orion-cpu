package orion.cpu.views.tapestry.services;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.LinkCreationListener;
import org.apache.tapestry5.services.Request;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Слушатель создания ссылок.
 * Сохраняет значение параметра menudata из HTTP запроса, если он не был определен
 * @author sl
 */
public class MenupathLinkCreationListener implements LinkCreationListener {

    private static final String MENUPATH="menupath";
    private final Request request;

    public MenupathLinkCreationListener(Request request) {
        this.request = Defense.notNull(request, "request");
    }

    @Override
    public void createdPageRenderLink(Link link) {
        String menudata = link.getParameterValue(MENUPATH);
        if (menudata == null || menudata.length() == 0) {
            menudata = request.getParameter(MENUPATH);
            if (menudata != null && menudata.length() != 0) {
                link.addParameter(MENUPATH, request.getParameter(MENUPATH));
            }
        }
    }

    @Override
    public void createdComponentEventLink(Link link) {
    }
}
