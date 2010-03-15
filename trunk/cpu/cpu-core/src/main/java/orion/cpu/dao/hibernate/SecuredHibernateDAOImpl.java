package orion.cpu.dao.hibernate;

import java.io.Serializable;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import orion.cpu.security.services.ExtendedAuthorizer;

/**
 * Расширяет OrionHibernateGenericDAO для поддержки Row Level Security
 * Берет из ExtendedAuthorizer ограничения <b>только на выбор данных</b>
 * @param <T> the entity class related to this DAO.
 * @param <K> the type of the field that represents the entity class' primary key.
 * @author sl
 */
public class SecuredHibernateDAOImpl<T, K extends Serializable> extends OrionHibernateGenericDAO<T, K> {

    private ExtendedAuthorizer authorizer = null;

    /**
     * @param clasz тип сущности
     * @param sessionFactory 
     * @param authorizer Если null то ведет себя как OrionGenericDAO
     * @author sl
     */
    public SecuredHibernateDAOImpl(Class<T> clasz, SessionFactory sessionFactory, ExtendedAuthorizer authorizer) {
        super(clasz, sessionFactory);
        this.authorizer = authorizer;
    }

    @Override
    public Criteria createCriteria() {
        Criteria c = super.createCriteria();
        if (authorizer != null) {
            authorizer.addConstraintsToCriteria(c);
        }
        return c;
    }
}
