package br.com.arsmachina.authentication.controller.impl;

import br.com.arsmachina.authentication.controller.PermissionGroupController;
import br.com.arsmachina.authentication.dao.PermissionGroupDAO;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.PermissionGroup;
import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.controllers.event.AfterFindByNameEv;
import orion.cpu.controllers.impl.BaseController;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.services.DefaultControllerListeners;

/**
 * {@link PermissionGroupController} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class PermissionGroupControllerImpl extends BaseController<PermissionGroup, Integer>
        implements PermissionGroupController {

    private PermissionGroupDAO dao;

    /**
     * Single constructor of this class.
     *
     * @param daoSource DAO source service. It cannot be <code>null</code>.
     * @param defaultControllerListeners
     */
    public PermissionGroupControllerImpl(DAOSource daoSource,
            DefaultControllerListeners defaultControllerListeners) {
        super(PermissionGroup.class, daoSource, defaultControllerListeners);
        this.dao = (PermissionGroupDAO) (Object) daoSource.get(PermissionGroup.class);
    }

    /**
     * Invokes <code>dao.findByName()<code>.
     * @param name a {@link String}.
     * @return a {@link PermissionGroup} or <code>null</code>.
     * @see br.com.arsmachina.authentication.dao.PermissionGroupDAO#findByName(java.lang.String)
     */
    @Override
    public PermissionGroup findByName(String name) {
        try {
            PermissionGroup l = dao.findByName(name);
            return processAfterEvent(new AfterFindByNameEv<PermissionGroup>(l, name));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }
}
