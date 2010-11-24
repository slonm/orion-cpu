package br.com.arsmachina.authentication.controller;

import br.com.arsmachina.controller.Controller;
import orion.cpu.baseentities.AbstractRole;

/**
 * Базовый интерфейс контроллера ролей и пользователей
 * @param <T> тип абстрактной роли (роль или пользователь)
 * @author sl
 */
public interface AbstractRoleController<T extends AbstractRole<?>> extends Controller<T, Integer> {

    /**
     * Returns the user with a given login or <code>null</code> if no such user
     * exists.
     *
     * @param login a <code>String</code>.
     * @return an T (Role or User).
     */
    T findByLogin(String login);

    /**
     * Loads the user and their permissions with a given login or
     * <code>null</code> if no such user exists. This method prefetches the
     * user's permissions.
     *
     * @param login a <code>String</code>.
     * @return an T (Role or User).
     */
    T loadForAuthentication(String login);

    /**
     * Loads the user and their permissions with a given login or
     * <code>null</code> if no such user exists. This method prefetches the
     * user's permissions and roles.
     *
     * @param login a <code>String</code>.
     * @return an T (Role or User).
     */
    T loadEverything(String login);

    /**
     * Tells if some user with a given login exists.
     *
     * @param login a {@link String}. It cannot be null.
     *
     * @return a <code>boolean</code>.
     */
    boolean hasWithLogin(String login);
}
