package br.com.arsmachina.authentication.dao;

import br.com.arsmachina.dao.DAO;
import java.util.List;
import orion.cpu.baseentities.AbstractRole;

/**
 * DAO для ролей
 * @param <T> User or Role
 * @author Mihail Slobodyanuk
 */
public interface AbstractRoleDAO<T extends AbstractRole<?>> extends DAO<T, Integer> {

    /**
     * Returns the user with a given login or <code>null</code> if no such
     * user exists.
     *
     * @param login a <code>String</code>.
     * @return an T.
     */
    T findByLogin(String login);

    /**
     * Loads the user and their permissions with a given login or <code>null</code> if no such
     * user exists. This method prefetches the user's permissions.
     *
     * @param login a <code>String</code>.
     * @return an T.
     */
    T loadForAuthentication(String login);

    /**
     * Loads the user and their permissions with a given login or <code>null</code> if no such
     * user exists. This method prefetches the user's permissions and roles.
     *
     * @param login a <code>String</code>.
     * @return an T.
     */
    T loadEverything(String login);

    /**
     * Tells if some role with a given login exists.
     * @param login a {@link String}. It cannot be null.
     *
     * @return a <code>boolean</code>.
     */
    boolean hasWithLogin(String login);

}
