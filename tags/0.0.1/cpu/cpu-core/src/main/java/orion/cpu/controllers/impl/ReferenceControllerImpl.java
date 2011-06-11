package orion.cpu.controllers.impl;

import br.com.arsmachina.module.service.DAOSource;
import java.util.Collections;
import java.util.List;
import orion.cpu.controllers.ReferenceController;
import orion.cpu.controllers.event.*;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.dao.ReferenceDAO;
import orion.cpu.baseentities.ReferenceEntity;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Реализация контроллера для справочников
 * @param <T>
 * @author sl
 */
public class ReferenceControllerImpl<T extends ReferenceEntity<?>> extends NamedEntityControllerImpl<T>
        implements ReferenceController<T> {

    private ReferenceDAO<T> dao;

    public ReferenceControllerImpl(Class<T> clazz, DAOSource daoSource,
                DefaultControllerListeners defaultControllerListeners) {
        super(clazz, daoSource, defaultControllerListeners);
        this.dao = (ReferenceDAO<T>) getDao();
    }

    @Override
    public T findByKey(String name) {
        try {
            T l = dao.findByKey(name);
            return processAfterEvent(new AfterFindByKeyEv<T>(l, name));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public List<T> findByAliasToIsNullAndExceptExample(T example) {
        try {
            List<T> l = dao.findByAliasToIsNullAndExceptExample(example);
            return processAfterEvent(new AfterFindByAliasToIsNullAndExceptExampleEv<T>(l, example));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<T> findByAliasToIsNullAndIsNotObsoleteAndExceptExample(T example) {
        try {
            List<T> l = dao.findByAliasToIsNullAndIsNotObsoleteAndExceptExample(example);
            return processAfterEvent(new AfterFindByAliasToIsNullAndIsNotObsoleteAndExceptExampleEv<T>(l, example));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }
}
