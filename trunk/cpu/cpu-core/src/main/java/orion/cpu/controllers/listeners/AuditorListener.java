package orion.cpu.controllers.listeners;

import br.com.arsmachina.authentication.entity.User;
import orion.cpu.controllers.listenersupport.*;
import br.com.arsmachina.controller.Controller;
import java.sql.Timestamp;
import java.util.Calendar;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.controllers.event.base.*;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Установщик пользователя, который делает сохранение обьекта в свойствах самого обьекта
 * Только для BaseEntity. При ошибке исключений не вызывает.
 * @author sl
 */
public class AuditorListener implements ControllerEventsListener {

    private final ExtendedAuthorizer authorizer;

    public AuditorListener(ExtendedAuthorizer authorizer) {
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @Override
    public <T> void onControllerEvent(Controller<T, ?> controller, Class<T> entityClass,
            ControllerEvent event) {
        try {
            if (event instanceof AbstractBeforeModifyByObjectEv) {
                BaseEntity<?> be = ((BaseEntity<?>) ((AbstractBeforeModifyByObjectEv) event).getObject());
                if (authorizer.getUser().getId() != null) {
                    be.setFiller(authorizer.getUser());
                }
                if (be.getFillDateTime() == null) {
                    be.setFillDateTime(be.getModifyDateTime());
                }
                if (be.getFillDateTime() == null) {
                    be.setFillDateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                }
            }
        } catch (Throwable tw) {
        }
    }
}
