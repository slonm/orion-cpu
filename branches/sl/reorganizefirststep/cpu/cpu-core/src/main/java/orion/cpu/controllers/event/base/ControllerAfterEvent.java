package orion.cpu.controllers.event.base;

/**
 * Интерфейс события после-
 * @param <T> тип возвращаемого значения
 * @author sl
 */
public interface ControllerAfterEvent<T> extends ControllerEvent{

    T getReturnValue();

    void setReturnValue(T value);
}
