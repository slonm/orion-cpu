package ua.orion.web.mixins;

import javax.inject.Inject;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.MixinClasses;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;
import ua.orion.web.OrionWebSymbols;

/**
 * Подмешиватель для ActionLink и EventLink. Перед вызовом события показывает
 * диалог подтверждения действия. На базе JQuery-UI dialog
 * (http://jqueryui.com/demos/dialog http://www.linkexchanger.su/2009/95.html)
 *
 * @author slobodyanuk
 */
//TODO Добавить импорт ресурсов JQuery-UI
@MixinAfter
@Import(library = {"Confirmation-messages.js", "Confirmation.js"})
public class Confirmation {

    /**
     * Заголовок диалога
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String dialogTitle;
    /**
     * Текст кнопки подтверждения
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String confirm;
    /**
     * Текст кнопки отказа
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String decline;
    /**
     * Тело сообщения
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String body;
    @InjectContainer
    private ClientElement element;
    @Inject
    private JavaScriptSupport javascriptSupport;
    @Inject
    private ComponentResources resources;

    void beginRender(MarkupWriter writer) {
        JSONObject init = new JSONObject();
        init.put("id", element.getClientId());
        if (resources.isBound("dialogTitle")) {
            init.put("title", dialogTitle);
        }
        if (resources.isBound("confirm")) {
            init.put("confirm", confirm);
        }
        if (resources.isBound("decline")) {
            init.put("decline", decline);
        }
        if (resources.isBound("body")) {
            init.put("body", body);
        }
        if(resources.getContainerResources().isBound("zone")){
            init.put("ajax", true);
        }
        //javascriptSupport.addInitializerCall("oriConfirmation", init);
        writer.getElement().forceAttributes(MarkupConstants.ONCLICK, "javascript:return Ori.confirm(event, " + init.toCompactString() + ");");
    }
}
