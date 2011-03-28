package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractBeforeModifyByObjectEv;
import orion.cpu.security.OperationTypes;

/**
 * Событие перед обновлением
 * @param <T> тип объекта
 * @author sl
 */
public class BeforeUpdateEv<T> extends AbstractBeforeModifyByObjectEv<T>{

    public BeforeUpdateEv(T object) {
        super(object);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.UPDATE_OP;
    }
}
