package orion.cpu.controllers.impl.sys;

import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.controllers.impl.NamedEntityControllerImpl;
import orion.cpu.controllers.sys.StoredConstantController;
import orion.cpu.entities.sys.StoredConstant;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Реализация контроллера для StoredConstant
 * @author sl
 */
public class StoredConstantControllerImpl extends NamedEntityControllerImpl<StoredConstant>
        implements StoredConstantController {

    public StoredConstantControllerImpl(DAOSource daoSource,
                DefaultControllerListeners defaultControllerListeners) {
        super(StoredConstant.class, daoSource, defaultControllerListeners);
    }
}
