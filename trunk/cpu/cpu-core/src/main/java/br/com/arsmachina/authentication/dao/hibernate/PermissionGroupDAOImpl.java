package br.com.arsmachina.authentication.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import br.com.arsmachina.authentication.dao.PermissionGroupDAO;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.PermissionGroup;
import br.com.arsmachina.dao.SortCriterion;
import br.com.arsmachina.dao.hibernate.GenericDAOImpl;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link PermissionGroupDAO} implementation using Hibernate.
 * 
 * @author sl
 */
public class PermissionGroupDAOImpl extends GenericDAOImpl<PermissionGroup, Integer> implements
        PermissionGroupDAO {

    /**
     * Single constructor of this class.
     *
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     */
    public PermissionGroupDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * @see br.com.arsmachina.authentication.dao.PermissionGroupDAO#findByName(java.lang.String)
     */
    @Override
    public PermissionGroup findByName(String name) {

        final Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", name));

        return (PermissionGroup) criteria.uniqueResult();

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

}
