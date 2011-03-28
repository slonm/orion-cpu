package orion.cpu.services.factory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.controllers.impl.BaseController;
import orion.cpu.controllers.impl.ReferenceControllerImpl;
import orion.cpu.baseentities.ReferenceEntity;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Фабрика создает контроллер TransactControllerImpl {@link ReferenceControllerFactory}.
 *
 * @author sl
 */
public class ReferenceControllerFactory extends BaseControllerFactory {

    /**
     * Single constructor of this class.
     *
     * @param defaultControllerListeners
     * @param daoSource a {@link DAOSource}. It cannot be null.
     */
    public ReferenceControllerFactory(DefaultControllerListeners defaultControllerListeners,
            DAOSource daoSource) {
        super(defaultControllerListeners, daoSource);
    }

    /**
     * Builds a {@link Controller} instance given a corresponding {@link DAO}.
     *
     * @param <T> an entity class.
     * @param entityClass класс сущности
     * @param daoSource
     * @param defaultControllerListeners 
     * @return a {@link Controller}.
     */
    @Override
    protected <T> BaseController<T, ?> build(Class<T> entityClass, DAOSource daoSource,
            DefaultControllerListeners defaultControllerListeners) {
        if (ReferenceEntity.class.isAssignableFrom(entityClass)) {
            return new ReferenceControllerImpl(entityClass, daoSource, defaultControllerListeners);
        }
        return null;
    }
}
