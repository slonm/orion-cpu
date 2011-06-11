package orion.cpu.views.tapestry.services;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.services.LinkCreationListener;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.URLEncoder;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Слушатель создания ссылок.
 * Сохраняет значение параметра menudata из HTTP запроса, если он не был определен
 * @author sl
 */
public class MenupathLinkCreationListener implements LinkCreationListener {

    private static final String MENUPATH = "menupath";
    private final Request request;
    private final Class<?> clasz;
    private final URLEncoder _URLEncoder;

    public MenupathLinkCreationListener(Request request,
            URLEncoder _URLEncoder,
            @Inject @Value("${cpumenu.navigatorpage}") Class<?> clasz) {
        this.request = Defense.notNull(request, "request");
        this._URLEncoder = Defense.notNull(_URLEncoder, "_URLEncoder");
        this.clasz = Defense.notNull(clasz, "clasz");
    }

    @Override
    public void createdPageRenderLink(Link link) {
        String linkMenupath = link.getParameterValue(MENUPATH);
        String newMenupath = null;
        //Если нет параметра menupath, то предположим что это страница
        //навигатора и у нее в activation context хранится путь меню
        if (linkMenupath == null) {
            String[] ac = link.toURI().split("/");
            boolean isNavigator = false;
            for (String s : ac) {
                //Ищем страницу навигатора
                if (clasz.getSimpleName().equalsIgnoreCase(s)) {
                    isNavigator = true;
                }
                //Путь меню начинается со Start
                if (s.startsWith("Start") && isNavigator) {
                    newMenupath = _URLEncoder.decode(s.replaceAll(";.*$", ""));
                    break;
                }
            }
        }
        if (newMenupath == null) {
            newMenupath = request.getParameter(MENUPATH);
        }
        if (linkMenupath == null && newMenupath != null) {
            link.addParameter(MENUPATH, newMenupath);
        }
    }

    @Override
    public void createdComponentEventLink(Link link) {
        createdPageRenderLink(link);
    }
}
