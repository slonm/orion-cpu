package br.com.arsmachina.authentication.controller;

import br.com.arsmachina.authentication.entity.User;

/**
 * Controller definition for {@link User}.
 * 
 * @author sl
 */
public interface UserController extends AbstractRoleController<User> {

    /**
     * Returns the user with a given login and password or <code>null</code> if
     * no such user exists.
     *
     * @param login a <code>String</code>.
     * @param password
     * @return User
     */
    User findByLoginAndPassword(String login, String password);

    /**
     * Перегружен потому, что иначе не вызывается из UserDetailsServiceImpl
     * @param login a <code>String</code>.
     * @return User
     
    @Override
    User findByLogin(String login);*/
}
