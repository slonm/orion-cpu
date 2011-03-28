package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractAfterModifyByObjectEv;
import orion.cpu.security.OperationTypes;

/**
 * Событие после удаления указанного объекта
 * @param <T> тип объекта
 * @author sl
 */
public class AfterDeleteByObjectEv<T> extends AbstractAfterModifyByObjectEv<T> {

    public AfterDeleteByObjectEv(T object) {
        super(null, object);
    }

    @Override
    public String getOperationType() {
        return OperationTypes.REMOVE_OP;
    }
}
