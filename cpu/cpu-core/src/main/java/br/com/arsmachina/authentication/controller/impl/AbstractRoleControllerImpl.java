package br.com.arsmachina.authentication.controller.impl;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.dao.AbstractRoleDAO;
import br.com.arsmachina.module.service.DAOSource;
import java.util.Collections;
import java.util.List;
import orion.cpu.baseentities.AbstractRole;
import orion.cpu.controllers.event.*;
import orion.cpu.controllers.impl.BaseController;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.services.DefaultControllerListeners;

/**
 * {@link UserController} implementation.
 * 
 * @param <T>
 * @author sl
 */
public abstract class AbstractRoleControllerImpl<T extends AbstractRole<?>> extends BaseController<T, Integer>
        implements AbstractRoleController<T> {

    private AbstractRoleDAO<T> dao;

    /**
     * Single constructor of this class.
     *
     * @param clasz abstractRole type. It cannot be <code>null</code>.
     * @param daoSource DAO source service. It cannot be <code>null</code>.
     * @param defaultControllerListeners 
     */
    public AbstractRoleControllerImpl(Class<T> clasz, DAOSource daoSource,
            DefaultControllerListeners defaultControllerListeners) {

        super(clasz, daoSource, defaultControllerListeners);
        this.dao = (AbstractRoleDAO<T>) (Object) daoSource.get(clasz);
    }

    @Override
    public T findByLogin(String login) {
        try {
            T l = dao.findByLogin(login);
            return processAfterEvent(new AfterFindByLoginEv<T>(l, login));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public T loadForAuthentication(String login) {
        try {
            T l = dao.findByLogin(login);
            return processAfterEvent(new AfterLoadForAuthenForAuthenticationEv<T>(l, login));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public T loadEverything(String login) {
        try {
            T l = dao.loadEverything(login);
            return processAfterEvent(new AfterLoadEverythingEv<T>(l, login));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public boolean hasWithLogin(String login) {
        try {
            Boolean l = dao.hasWithLogin(login);
            return processAfterEvent(new AfterHasWithLoginEv(l, login));
        } catch (AbortControllerEventException ex) {
            return false;
        }
    }

    @Override
    public List<Class<?>> findPermittedTypes(T abstractRole, String permissionType) {
        try {
            List<Class<?>> l = dao.findPermittedTypes(abstractRole, permissionType);
            return processAfterEvent(new AfterFindPermittedTypesEv<T>(l, abstractRole, permissionType));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public <X> List<X> findPermittedObjects(Class<X> objectType, T abstractRole, String permissionType) {
        try {
            List<X> l = dao.findPermittedObjects(objectType, abstractRole, permissionType);
            return processAfterEvent(new AfterFindPermittedObjectsEv<T, X>(l, objectType, abstractRole, permissionType));
        } catch (AbortControllerEventException ex) {
            return Collections.EMPTY_LIST;
        }
    }
}
