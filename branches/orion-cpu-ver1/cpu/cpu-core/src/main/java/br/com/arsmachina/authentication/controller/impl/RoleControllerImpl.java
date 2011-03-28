package br.com.arsmachina.authentication.controller.impl;

import br.com.arsmachina.authentication.controller.RoleController;
import br.com.arsmachina.authentication.entity.Role;
import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.services.DefaultControllerListeners;

/**
 * Контроллер для ролей
 * @author sl
 */
public class RoleControllerImpl extends AbstractRoleControllerImpl<Role>
        implements RoleController {

    public RoleControllerImpl(DAOSource daoSource,
                DefaultControllerListeners defaultControllerListeners) {
        super(Role.class, daoSource, defaultControllerListeners);
    }
}
