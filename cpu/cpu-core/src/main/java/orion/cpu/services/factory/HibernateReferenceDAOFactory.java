package orion.cpu.services.factory;

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.factory.DAOFactory;
import org.hibernate.SessionFactory;
import orion.cpu.dao.hibernate.HibernateReferenceDAO;
import orion.cpu.baseentities.ReferenceEntity;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Фабрика создает объект HibernateReferenceDAO
 * @author sl
 */
public class HibernateReferenceDAOFactory implements DAOFactory {

    final private SessionFactory sessionFactory;
    final private ExtendedAuthorizer authorizer;
    /**
     * Single constructor of this class.
     *
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     * @param authorizer 
     */
    public HibernateReferenceDAOFactory(SessionFactory sessionFactory, ExtendedAuthorizer authorizer) {
        this.sessionFactory = Defense.notNull(sessionFactory, "sessionFactory");
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> DAO<T, ?> build(Class<T> entityClass) {
        DAO<T, ?> dao = null;
        if (sessionFactory.getClassMetadata(entityClass) != null &&
                ReferenceEntity.class.isAssignableFrom(entityClass)) {
            dao = new HibernateReferenceDAO(entityClass, sessionFactory, authorizer);
        }
        return dao;
    }
}
