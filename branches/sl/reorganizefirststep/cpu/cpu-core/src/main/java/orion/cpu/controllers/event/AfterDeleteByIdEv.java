package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractAfterModifyByIdEv;
import java.io.Serializable;
import orion.cpu.security.OperationTypes;

/**
 * Событие после удаления по Id
 * @author sl
 */
public class AfterDeleteByIdEv extends AbstractAfterModifyByIdEv<Object>{

    public AfterDeleteByIdEv(Serializable id) {
        super(null, id);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.REMOVE_OP;
    }
}
