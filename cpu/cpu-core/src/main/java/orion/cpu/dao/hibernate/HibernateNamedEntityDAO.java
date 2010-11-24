package orion.cpu.dao.hibernate;

import br.com.arsmachina.authentication.dao.hibernate.Constants;
import br.com.arsmachina.dao.SortCriterion;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import orion.cpu.baseentities.NamedEntity;
import orion.cpu.dao.NamedEntityDAO;

/**
 * Hibernate реализация DAO для NamedEntity
 * @param <T> тип справочника
 * @author sl 
 */
@SuppressWarnings("unchecked")
public class HibernateNamedEntityDAO<T extends NamedEntity<?>> extends OrionHibernateGenericDAO<T, Integer>
        implements NamedEntityDAO<T> {

    public HibernateNamedEntityDAO(Class<T> clasz, SessionFactory sessionFactory) {
        super(clasz, sessionFactory);
    }

    @Override
    public List<T> findByName(String name) {
        return createCriteria().add(Restrictions.eq("name", name)).list();
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
