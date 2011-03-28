package orion.cpu.services.factory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.factory.ControllerFactory;
import br.com.arsmachina.module.service.DAOSource;
import java.io.Serializable;
import orion.cpu.controllers.impl.BaseController;
import orion.cpu.services.DefaultControllerListeners;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Фабрика создает контроллер TransactControllerImpl
 * @author sl
 */
public class BaseControllerFactory implements ControllerFactory {

    private final DefaultControllerListeners defaultControllerListeners;
    private final DAOSource daoSource;

    /**
     * Single constructor of this class.
     *
     * @param defaultControllerListeners
     * @param daoSource a {@link DAOSource}. It cannot be null.
     */
    public BaseControllerFactory(DefaultControllerListeners defaultControllerListeners,
            DAOSource daoSource) {
        this.defaultControllerListeners = Defense.notNull(defaultControllerListeners, "defaultControllerListeners");
        this.daoSource = Defense.notNull(daoSource, "daoSource");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Controller<T, ?> build(Class<T> entityClass) {
        BaseController<T, ?> controller = null;
        final DAO<T, Serializable> dao = daoSource.get(entityClass);
        if (dao != null) {
            controller = build(entityClass, daoSource, defaultControllerListeners);
        }
        return controller;
    }

    protected <T> BaseController<T, ?> build(Class<T> entityClass, DAOSource daoSource,
            DefaultControllerListeners defaultControllerListeners) {
        BaseController<T, Serializable> c =
                new BaseController<T, Serializable>(entityClass, daoSource, defaultControllerListeners);
        return c;
    }
}
