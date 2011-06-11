package br.com.arsmachina.authentication.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import br.com.arsmachina.authentication.dao.PermissionDAO;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.dao.SortCriterion;
import br.com.arsmachina.dao.hibernate.GenericDAOImpl;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link PermissionDAO} implementation using Hibernate
 * 
 * @author sl
 */
public class PermissionDAOImpl extends GenericDAOImpl<Permission, Integer> implements PermissionDAO {

    /**
     * Single constructor of this class.
     *
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     */
    public PermissionDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
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

    /**
     * @see br.com.arsmachina.authentication.dao.PermissionDAO#findByName(java.lang.String)
     */
    @Override
    public Permission findByName(String name) {

        final Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", name));

        return (Permission) criteria.uniqueResult();

    }

    @Override
    public Permission findBySubjectAndType(Class<?> subject, String type) {
        Defense.notNull(subject, "subject");
        final Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("subjectClassName", subject.getName()));
        criteria.add(Restrictions.eq("permissionType", type));
        return (Permission) criteria.uniqueResult();
    }
}
