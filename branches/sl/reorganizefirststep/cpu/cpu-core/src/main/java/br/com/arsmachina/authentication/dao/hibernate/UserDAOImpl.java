package br.com.arsmachina.authentication.dao.hibernate;

import org.hibernate.*;
import br.com.arsmachina.authentication.controller.PasswordEncrypter;
import br.com.arsmachina.authentication.dao.UserDAO;
import br.com.arsmachina.authentication.entity.User;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link UserDAO} implementation using Hibernate
 * @author Mihail Slobodyanuk
 */
public class UserDAOImpl extends AbstractRoleDAOImpl<User> implements
        UserDAO {

    private PasswordEncrypter passwordEncrypter;

    /**
     * Single constructor of this class.
     *
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     * @param passwordEncrypter a {@link PasswordEncrypter}. It cannot be null.
     */
    public UserDAOImpl(SessionFactory sessionFactory,
            PasswordEncrypter passwordEncrypter) {

        super(User.class, sessionFactory);
        this.passwordEncrypter = Defense.notNull(passwordEncrypter, "passwordEncrypter");
    }

    /**
     * Finds the user with a given login and password. The login search is
     * case-insensitive.
     *
     * @see br.com.arsmachina.authentication.dao.UserDAO#findByLoginAndPassword(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public User findByLoginAndPassword(String login, String password) {
        Session session = getSession();
        Query query =
                session.createQuery("from User where lowercase(login) = :login and " + "password = :password");
        query.setParameter("login", login.toLowerCase());
        query.setParameter("password", passwordEncrypter.encrypt(password));
        return (User) query.uniqueResult();
    }

    @Override
    protected void loadEverything(User user) {
        // force the loading of the user's roles
        user.getRoles().size();
    }
}
