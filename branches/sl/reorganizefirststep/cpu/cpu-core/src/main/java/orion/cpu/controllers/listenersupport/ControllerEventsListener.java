package orion.cpu.controllers.listenersupport;

import orion.cpu.controllers.event.base.ControllerEvent;
import br.com.arsmachina.controller.Controller;

/**
 * Интерфейс слушателя событий контроллера
 * @author sl
 */
public interface ControllerEventsListener {

    /**
     * Вызывается обсервером при возникновении любого события контроллера.
     *
     * @param <T>
     * @param controller контроллер
     * @param entityClass
     * @param event событие
     */
    <T> void onControllerEvent(Controller<T, ?> controller, Class<T> entityClass, ControllerEvent event);
}
