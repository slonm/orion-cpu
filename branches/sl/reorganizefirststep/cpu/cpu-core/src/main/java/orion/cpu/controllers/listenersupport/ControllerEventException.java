package orion.cpu.controllers.listenersupport;

import orion.cpu.controllers.event.base.ControllerEvent;

/**
 * Исключение при выполнении операции контроллером.
 * Это исключение хранит в себе тип события или само событие, которое его вызвало.
 * @author sl
 */
public class ControllerEventException extends RuntimeException {

    private final ControllerEvent event;
    private final Class<? extends ControllerEvent> eventType;

    public ControllerEventException(String message, Class<? extends ControllerEvent> eventType) {
        super(message);
        this.eventType = eventType;
        this.event = null;
    }

    public ControllerEventException(Class<? extends ControllerEvent> eventType) {
        super(String.format("Handled on event %s ", eventType.getName()));
        this.eventType = eventType;
        this.event = null;
    }

    public ControllerEventException(String message, ControllerEvent event) {
        super(message);
        this.event = event;
        this.eventType = event.getClass();
    }

    public ControllerEventException(ControllerEvent event) {
        super(String.format("Handled on event %s ", event.getClass().getName()));
        this.event = event;
        this.eventType = event.getClass();
    }

    public ControllerEvent getEvent() {
        return event;
    }

    public Class<? extends ControllerEvent> getEventType() {
        return eventType;
    }
}
