package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.authentication.entity.User;
import java.util.*;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 * Страница ошибок. Ошибка определяется activation context.
 * Возможные ошибки:
 * failed ошибка логина.
 * accessdenied доступ запрещен.
 * @author sl
 */
@SuppressWarnings("unused")
public class ErrorReport implements ExceptionReporter {

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
    @Persist(PersistenceConstants.FLASH)
    private String errorMessage;
    @Inject
    private Messages messages;

    public void onActivate(EventContext ec) {
        if (ec.getCount() == 0) {
            return;
        }
        String errorType = ec.get(String.class, 0);
        if (messages.contains(MESSAGE_ERROR_PREFIX + errorType)) {
            errorMessage = messages.get(MESSAGE_ERROR_PREFIX + errorType);
        } else {
            errorMessage = String.format(messages.get(MESSAGE_ERROR_PREFIX
                    + ERROR_MESSAGE_NOT_FOUND), errorType);
        }
    }

    @Override
    public void reportException(Throwable exception) {
        errorMessage = messages.get(MESSAGE_ERROR_PREFIX + ACCESS_DENIED)  
                + exception.getMessage();
    }
}
