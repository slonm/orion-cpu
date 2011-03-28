package orion.cpu.controllers.listeners;

import orion.cpu.controllers.listenersupport.*;
import br.com.arsmachina.controller.Controller;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import orion.cpu.controllers.event.base.*;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Слушатель событий контроллера для управления транзакциями.
 * Делает commit текущей транзакции после всез событий изменяющих данные
 * @author sl
 */
public class CommitTransactionListener implements ControllerEventsListener {

    private final HibernateSessionManager manager;

    public CommitTransactionListener(HibernateSessionManager manager) {
        this.manager = Defense.notNull(manager, "manager");
    }

    @Override
    public <T> void onControllerEvent(Controller<T, ?> controller, Class<T> entityClass,
            ControllerEvent event) {
        if (event instanceof ModifyEv && event instanceof ControllerAfterEvent) {
            manager.commit();
        }
    }
}
