package br.com.arsmachina.authentication.dao;

import br.com.arsmachina.authentication.entity.User;

/**
 * Data access object (DAO) for {@link User}.
 * @author Mihail Slobodyanuk
 */
public interface UserDAO extends AbstractRoleDAO<User> {

    /**
     * Returns the user with a given login and password or <code>null</code> if no such
     * user exists.
     *
     * @param login a <code>String</code>.
     * @param password a <code>String</code>.
     * @return an {@link User}.
     */
    User findByLoginAndPassword(String login, String password);
}
