package orion.cpu.views.tapestry.pages;

import java.util.*;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * Страница ошибок. Ошибка определяется activation context.
 * Возможные ошибки:
 * failed ошибка логина.
 * accessdenied доступ запрещен.
 * @author sl
 */
@SuppressWarnings("unused")
public class ErrorReport {

    /**
     * Префикс имени сообщения об ошибке
     */
    public static final String MESSAGE_ERROR_PREFIX = "message.error.";
    /**
     * Сообщение об ошибке не найдено
     */
    public static final String ERROR_MESSAGE_NOT_FOUND = "messagenotfound";
    /**
     * Ошибка логина
     */
    public static final String LOGIN_FAILED = "loginfailed";
    /**
     * Доступ запрещен
     */
    public static final String ACCESS_DENIED = "accessdenied";
    @Property(write = false)
    private String errorMessage;
    @Inject
    private Messages messages;
    @Inject
    private PageRenderLinkSource pageRenderLinkSource;

    public Object onActivate(EventContext ec) {
        if (ec.getCount() == 0) {
            return "";
        }
        String errorType = ec.get(String.class, 0);
        if (messages.contains(MESSAGE_ERROR_PREFIX + errorType)) {
            errorMessage = messages.get(MESSAGE_ERROR_PREFIX + errorType);
        } else {
            errorMessage = String.format(messages.get(MESSAGE_ERROR_PREFIX +
                    ERROR_MESSAGE_NOT_FOUND), errorType);
        }
        return null;
    }

    public Link getErrorReportLink(String errorType, Object... ac) {
        List<Object> list = new ArrayList<Object>();
        list.add(errorType);
        list.addAll(Arrays.asList(ac));
        return pageRenderLinkSource.createPageRenderLinkWithContext(ErrorReport.class, list.toArray());
    }
}
