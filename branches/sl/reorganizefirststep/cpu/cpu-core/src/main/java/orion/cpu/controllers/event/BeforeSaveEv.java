package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractBeforeModifyByObjectEv;
import orion.cpu.security.OperationTypes;

/**
 * Событие перед сохранением
 * @param <T> тип объекта
 * @author sl
 */
public class BeforeSaveEv<T> extends AbstractBeforeModifyByObjectEv<T>{

    public BeforeSaveEv(T object) {
        super(object);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.STORE_OP;
    }
}
