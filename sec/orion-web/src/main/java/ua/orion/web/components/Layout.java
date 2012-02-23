package ua.orion.web.components;

import java.io.IOException;
import java.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import ua.orion.cpu.core.security.services.ThreadRole;
import ua.orion.web.OrionWebSymbols;

/**
 * Layout component for pages.
 */
@Import(stack = {"orion"}, stylesheet = {"layout/layout.css", "context:webcontent/jquery-ui/css/smoothness/jquery-ui-1.8.16.custom.css", "../css/cpu_web.css", "context:webcontent/jquery-ui/css/jquery.ui.selectmenu.css"})
@SuppressWarnings("unused")
public class Layout {

    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block meta;
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block style;
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block script;
    @Environmental
    private JavaScriptSupport javaScriptSupport;
    //Подключение javascript. Используется Inject, а не import для того, чтобы 
    //в дальнейшем подключить их в правильном порядке
    @Inject
    @Path("../jquery.js")
    private Asset jQueryLibrary;
    @Inject
    @Path("context:webcontent/jquery-ui/js/jquery-ui-1.8.16.custom.min.js")
    private Asset jQueryUILibrary;
    @Inject
    @Path("context:webcontent/jquery-ui/js/jquery.ui.selectmenu.js")
    private Asset jQueryUISelectMenu;
    @Inject
    @Path("context:webcontent/jquery-ui/js/jquery-ui-i18n.js")
    private Asset jQueryUILocalization;
    @Inject
    @Path("../jquery.noconflict.js")
    private Asset jQueryNoConflictLibrary;
    @Inject
    @Path("../jquery.cookie.js")
    private Asset jQueryCookieLibrary;
    /**
     * Используется для генерирования красивых ToolTip-ов
     * Библиотека загружена лишь с этой функциональностью. Нужно больше - http://flowplayer.org/tools/. Сохранить с тем же именем.
     */
    @Inject
    @Path("../jquery.tools.min.js")
    private Asset jQueryToolsLibrary;
    @Inject
    @Path("../ui-interface.js")
    private Asset jQueryUIInterfaceLibrary;
    @Inject
    @Path("../easyTooltip.js")
    private Asset jQueryToolTipLibrary;
    @Inject
    @Path("../cpu_effects.js")
    private Asset jQueryCPUEffectsLibrary;
    @Inject
    @Symbol(OrionWebSymbols.UI_INTERFACE)
    @Property
    private String uiInterface;
    /**
     * Page navigation menu
     */
    @Parameter(defaultPrefix = "literal")
    @Property(write = false)
    private Object menudata;
    @Inject
    private Request request;
    @Inject
    private Response response;
    @Inject
    private ThreadRole thRole;
    @Inject
    private ComponentResources cr;

    public String getRole() {
        return thRole.getRole();
    }

    public String getUser() {
        //TODO Возвращать ФИО
        return SecurityUtils.getSubject().getPrincipals().oneByType(String.class);
    }

    //Подключение библиотек при старте ренрдеринга 
    @SetupRender
    public void SetupRender() {
        if (!cr.isBound("menudata")) {
            menudata = request.getParameter("menupath") == null ? "Start"
                    : request.getParameter("menupath");
        }
        List<Asset> libraries = Arrays.<Asset>asList(jQueryLibrary, jQueryUILibrary, jQueryUILocalization, jQueryNoConflictLibrary, jQueryCookieLibrary, jQueryToolsLibrary, jQueryUIInterfaceLibrary, jQueryToolTipLibrary, jQueryUISelectMenu, jQueryCPUEffectsLibrary);
        for (Asset library : libraries) {
            javaScriptSupport.importJavaScriptLibrary(library);
        }
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
