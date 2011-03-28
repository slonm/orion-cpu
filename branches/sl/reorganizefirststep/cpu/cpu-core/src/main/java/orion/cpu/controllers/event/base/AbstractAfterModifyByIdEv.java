package orion.cpu.controllers.event.base;

import java.io.Serializable;

/**
 * Базовый класс событий после модификации по Id
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public abstract class AbstractAfterModifyByIdEv<T> extends AbstractModifyByIdEv implements ControllerAfterEvent<T> {

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

    public AbstractAfterModifyByIdEv(T returnValue, Serializable id) {
        super(id);
        this.returnValue = returnValue;
    }
}
