package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractAfterModifyByObjectEv;
import orion.cpu.security.OperationTypes;

/**
 * Событие после сохранения
 * @param <T> тип объекта
 * @author sl
 */
public class AfterSaveEv<T> extends AbstractAfterModifyByObjectEv<T>{

    public AfterSaveEv(T object) {
        super(null, object);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.STORE_OP;
    }
}
