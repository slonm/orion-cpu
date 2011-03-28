package br.com.arsmachina.authentication.dao;

import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.PermissionGroup;
import br.com.arsmachina.dao.DAO;

/**
 * Data access object (DAO) for {@link PermissionGroup}.
 * 
 * @author sl
 */
public interface PermissionGroupDAO extends DAO<PermissionGroup, Integer> {

    /**
     * Returns the permission group with the given name or <code>null</code> if there is no such one.
     *
     * @param name a {@link String}.
     * @return a {@link PermissionGroup} or <code>null</code>.
     */
    public PermissionGroup findByName(String name);

}
