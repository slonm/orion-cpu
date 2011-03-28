package orion.cpu.controllers.event.base;

import orion.cpu.security.OperationTypes;

/**
 * Базовый класс событий после просмотра
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public abstract class AbstractAfterViewEv<T> implements ControllerAfterEvent<T>{

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

    public AbstractAfterViewEv(T returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String getOperationType(){
        return OperationTypes.READ_OP;
    }
}
