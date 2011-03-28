package br.com.arsmachina.authentication.dao;

import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.dao.DAO;

/**
 * Data access object (DAO) for {@link Permission}.
 * 
 * @author sl
 */
public interface PermissionDAO extends DAO<Permission, Integer> {

    /**
     * Returns the permission with the given name or <code>null</code> if there is no such one.
     * @param name a {@link String}.
     * @return a {@link Permission} or <code>null</code>.
     */
    public Permission findByName(String name);

    /**
     * Returns the permission with the given subject and type or <code>null</code> if there is no such one.
     * @param subject
     * @param type
     * @return a {@link Permission} or <code>null</code>.
     */
    public Permission findBySubjectAndType(Class<?> subject, String type);
}
