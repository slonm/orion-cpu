package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.ControllerEvent;
import orion.cpu.security.OperationTypes;

/**
 * Событие до удаления объекта из сессии
 * @param <T> тип объекта
 * @author sl
 */
public class BeforeEvictEv<T> implements ControllerEvent{

    private T object;

    public BeforeEvictEv(T object) {
        this.object = object;
    }

    @Override
    public String getOperationType() {
        return "EVICT";
    }

    public void setObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
