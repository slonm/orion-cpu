package br.com.arsmachina.authentication.controller.impl;

import br.com.arsmachina.authentication.controller.PermissionController;
import br.com.arsmachina.authentication.dao.PermissionDAO;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.controllers.event.*;
import orion.cpu.controllers.impl.BaseController;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.services.DefaultControllerListeners;

/**
 * {@link PermissionController} implementation.
 * 
 * @author sl
 */
public class PermissionControllerImpl extends BaseController<Permission, Integer> implements
        PermissionController {

    private PermissionDAO dao;

    /**
     * Single constructor of this class.
     *
     * @param daoSource DAO source service. It cannot be <code>null</code>.
     * @param defaultControllerListeners
     */
    public PermissionControllerImpl(DAOSource daoSource,
            DefaultControllerListeners defaultControllerListeners) {
        super(Permission.class, daoSource, defaultControllerListeners);
        this.dao = (PermissionDAO) (Object) daoSource.get(Permission.class);
    }

    /**
     * Invokes <code>dao.findByName()<code>.
     * @param name a {@link String}.
     * @return a {@link Permission} or <code>null</code>.
     * @see br.com.arsmachina.authentication.dao.PermissionDAO#findByName(java.lang.String)
     */
    @Override
    public Permission findByName(String name) {
        try {
            Permission l = dao.findByName(name);
            return processAfterEvent(new AfterFindByNameEv<Permission>(l, name));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }

    @Override
    public Permission findBySubjectAndType(Class<?> subject, String type) {
        try {
            Permission l = dao.findBySubjectAndType(subject, type);
            return processAfterEvent(new AfterFindBySubjectAndTypeEv<Permission>(l, subject, type));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }
}
