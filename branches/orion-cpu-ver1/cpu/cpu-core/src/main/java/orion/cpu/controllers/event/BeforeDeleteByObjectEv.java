package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractBeforeModifyByObjectEv;
import orion.cpu.security.OperationTypes;

/**
 * Событие после удаления объекта
 * @param <T> тип объекта
 * @author sl
 */
public class BeforeDeleteByObjectEv<T> extends AbstractBeforeModifyByObjectEv<T>{

    public BeforeDeleteByObjectEv(T object) {
        super(object);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.REMOVE_OP;
    }
}
