package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractBeforeModifyByIdEv;
import java.io.Serializable;
import orion.cpu.security.OperationTypes;

/**
 * Событие перед удалением по Id
 * @author sl
 */
public class BeforeDeleteByIdEv extends AbstractBeforeModifyByIdEv{

    public BeforeDeleteByIdEv(Serializable id) {
        super(id);
    }

    @Override
    public String getOperationType(){
        return OperationTypes.REMOVE_OP;
    }
}
