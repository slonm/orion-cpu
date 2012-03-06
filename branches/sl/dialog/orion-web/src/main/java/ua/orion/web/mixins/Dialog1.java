package ua.orion.web.mixins;

import javax.inject.Inject;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;
import ua.orion.web.OrionWebSymbols;

/**
 * Делает зону всплывающим окном. На базе JQuery-UI dialog
 * (http://jqueryui.com/demos/dialog http://www.linkexchanger.su/2009/95.html)
 *
 * @author slobodyanuk
 */
//TODO Добавить импорт ресурсов JQuery-UI
public class Dialog1 {

    @Parameter(defaultPrefix = "literal", required = true)
    private String title;
    @Parameter(defaultPrefix = "literal")
    private String modal;
    @Parameter(defaultPrefix = "literal", value = "750")
    private String width;
    @Parameter(defaultPrefix = "literal")
    private String height;
    @Parameter(defaultPrefix = "literal", value = "symbol:" + OrionWebSymbols.UI_DIALOG_SHOW)
    private String show;
    @Parameter(defaultPrefix = "literal", value = "symbol:" + OrionWebSymbols.UI_DIALOG_HIDE)
    private String hide;
    @Parameter(defaultPrefix = "literal")
    private boolean closeOnEscape;
    @Parameter(defaultPrefix = "literal")
    private boolean draggable;
    @Parameter(defaultPrefix = "literal")
    private boolean resizable;
    @Parameter(defaultPrefix = "literal")
    private int zIndex;
    @Inject
    private Request request;
    @InjectContainer
    private ClientElement element;
    @Inject
    private JavaScriptSupport javascriptSupport;
    @Inject
    private Logger LOG;
    @Inject
    private ComponentResources resources;

    void setupRender() {
        JSONObject init = new JSONObject();
        init.put("autoOpen", false);
        init.put("title", title);
        if (resources.isBound("modal")) {
            init.put("modal", modal);
        }
        init.put("show", show);
        init.put("hide", hide);
        if (resources.isBound("height")) {
            init.put("height", height);
        }
        init.put("width", width);
        if (resources.isBound("closeOnEscape")) {
            init.put("closeOnEscape", closeOnEscape);
        }
        if (resources.isBound("draggable")) {
            init.put("draggable", draggable);
        }
        if (resources.isBound("height")) {
            init.put("resizable", resizable);
        }
        if (resources.isBound("zIndex")) {
            init.put("zIndex", zIndex);
        }
        javascriptSupport.addScript(InitializationPriority.LATE, "jQuery('#" + element.getClientId() + "').dialog(" + init.toString() + ");");
    }

    boolean beforeRenderBody() {
        return false;
    }
}
