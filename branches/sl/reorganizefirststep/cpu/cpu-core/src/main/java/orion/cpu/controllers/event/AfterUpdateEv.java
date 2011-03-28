package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractAfterModifyByObjectEv;
import orion.cpu.security.OperationTypes;

/**
 * Событие после обновления
 * @param <T> тип объекта
 * @author sl
 */
public class AfterUpdateEv<T> extends AbstractAfterModifyByObjectEv<T>{

    public AfterUpdateEv(T returnValue, T object) {
        super(returnValue, object);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.UPDATE_OP;
    }
}
