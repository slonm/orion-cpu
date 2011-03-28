package br.com.arsmachina.authentication.dao.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import br.com.arsmachina.authentication.dao.AbstractRoleDAO;
import br.com.arsmachina.authentication.dao.UserDAO;
import br.com.arsmachina.dao.SortCriterion;
import br.com.arsmachina.dao.hibernate.ConcreteDAOImpl;
import br.com.arsmachina.dao.hibernate.GenericDAOImpl;
import java.util.Collections;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import orion.cpu.baseentities.AbstractRole;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link UserDAO} implementation using Hibernate
 * @param <T>
 * @author Mihail Slobodyanuk
 */
public abstract class AbstractRoleDAOImpl<T extends AbstractRole<?>> extends ConcreteDAOImpl<T, Integer> implements
        AbstractRoleDAO<T> {

    private final Class<T> roleClass;

    /**
     * Single constructor of this class.
     *
     * @param roleClass
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     */
    public AbstractRoleDAOImpl(Class<T> roleClass, SessionFactory sessionFactory) {
        super(roleClass, sessionFactory);
        this.roleClass = Defense.notNull(roleClass, "roleClass");
    }

    public Class<T> getRoleClass() {
        return roleClass;
    }

    /**
     * Finds an user by its login. The search is case-insensitive.
     *
     * @see br.com.arsmachina.authentication.dao.UserDAO#findByLogin(java.lang.String)
     */
    @Override
    public T findByLogin(String login) {
        Session session = getSession();
        Criteria cr = session.createCriteria(getRoleClass()).
                add(Restrictions.eq("login", login.toLowerCase()));
        return (T) cr.uniqueResult();
    }

    @Override
    public T loadForAuthentication(String login) {
        Session session = getSession();
        Query query =
                session.createQuery("select u from " + getRoleClass().getSimpleName() + " u left join fetch u.permissionGroups " +
                "where u.login = :login");
        query.setParameter("login", login.toLowerCase());
        final T role = (T) query.uniqueResult();
        // force the loading of the user's permissions
        role.getPermissions();
        return role;
    }

    protected abstract void loadEverything(T role);

    @Override
    public T loadEverything(String login) {
        final T role = loadForAuthentication(login);
        // force the loading of the user's roles
        loadEverything(role);
        return role;
    }

    /**
     * Returns {@link Constants#ASCENDING_NAME_SORT_CRITERIA}.
     *
     * @see br.com.arsmachina.dao.hibernate.GenericDAOImpl#getDefaultSortCriteria()
     */
    @Override
    public SortCriterion[] getDefaultSortCriteria() {
        return Constants.ASCENDING_NAME_SORT_CRITERIA;
    }

    @Override
    public boolean hasWithLogin(String login) {
        Query query =
                getSession().createQuery(
                "select count(distinct u) from " + getRoleClass().getSimpleName() + " u where login = :login");
        query.setParameter("login", login.toLowerCase());
        Integer result = (Integer) query.uniqueResult();
        return result > 0;
    }

}
