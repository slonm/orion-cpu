package orion.cpu.controllers.impl;

import br.com.arsmachina.module.service.DAOSource;
import java.util.Collections;
import java.util.List;
import orion.cpu.baseentities.NamedEntity;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.controllers.event.*;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.dao.NamedEntityDAO;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Контроллер для NamedEntity
 * @param <T>
 * @author sl
 */
public class NamedEntityControllerImpl<T extends NamedEntity<?>> extends BaseController<T, Integer>
        implements NamedEntityController<T> {

    private NamedEntityDAO<T> dao;

    public NamedEntityControllerImpl(Class<T> clazz, DAOSource daoSource,
                DefaultControllerListeners defaultControllerListeners) {
        super(clazz, daoSource, defaultControllerListeners);
        this.dao = (NamedEntityDAO<T>) getDao();
    }

    @Override
    public List<T> findByName(String name) {
        try {
            List<T> l = dao.findByName(name);
            return processAfterEvent(new AfterFindByNameEv<List<T>>(l, name));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public T findByNameFirst(String name) {
        List<T> l = findByName(name);
        if (l.size() == 0) {
            return null;
        }
        return l.get(0);
    }
}
