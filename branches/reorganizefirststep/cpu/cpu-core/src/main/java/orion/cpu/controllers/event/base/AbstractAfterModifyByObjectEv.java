package orion.cpu.controllers.event.base;

/**
 * Базовый класс событий после модификации объекта
 * @param <T> тип объекта
 * @author sl
 */
public abstract class AbstractAfterModifyByObjectEv<T> extends AbstractModifyByObjectEv<T> implements ControllerAfterEvent<T> {

    protected T returnValue;

    /**
     * Get the value of returnValue
     *
     * @return the value of returnValue
     */
    @Override
    public T getReturnValue() {
        return returnValue;
    }

    /**
     * Set the value of returnValue
     *
     * @param returnValue new value of returnValue
     */
    @Override
    public void setReturnValue(T returnValue) {
        this.returnValue = returnValue;
    }

    public AbstractAfterModifyByObjectEv(T returnValue, T object) {
        super(object);
        this.returnValue = returnValue;
    }
}
