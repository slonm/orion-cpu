package orion.cpu.dao.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import orion.cpu.dao.ReferenceDAO;
import orion.cpu.baseentities.ReferenceEntity;
import orion.cpu.security.services.ExtendedAuthorizer;

/**
 * Hibernate реализация DAO справочников
 * @param <T> тип справочника
 * @author sl
 */
@SuppressWarnings("unchecked")
public class HibernateReferenceDAO<T extends ReferenceEntity<?>> extends HibernateNamedEntityDAO<T> implements ReferenceDAO<T> {

    public HibernateReferenceDAO(Class<T> clasz, SessionFactory sessionFactory, ExtendedAuthorizer authorizer) {
        super(clasz, sessionFactory, authorizer);
    }

    @Override
    public T findByKey(String name) {
        return (T) createCriteria().add(Restrictions.eq("key", name)).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<T> findByAliasToIsNullAndExceptExample(T example) {
        return createCriteria().add(Restrictions.isNull("aliasTo"))
                .add(Restrictions.not(createExample(example)))
                .list();
    }

    @Override
    public List<T> findByAliasToIsNullAndIsNotObsoleteAndExceptExample(T example) {
        return createCriteria().add(Restrictions.isNull("aliasTo"))
                .add(Restrictions.eq("isObsolete", false))
                .add(Restrictions.not(createExample(example)))
                .list();
    }
}
