package orion.cpu.services.factory;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.factory.DAOFactory;
import orion.cpu.dao.hibernate.SecuredHibernateDAOImpl;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link DefaultDAOFactory} implementation using Hibernate.
 * 
 * @author sl
 */
public class SecuredDAOFactory implements DAOFactory {

    final private SessionFactory sessionFactory;
    final private ExtendedAuthorizer authorizer;

    /**
     * Single constructor of this class.
     * 
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     * @param authorizer 
     */
    public SecuredDAOFactory(SessionFactory sessionFactory, ExtendedAuthorizer authorizer) {
        this.sessionFactory = Defense.notNull(sessionFactory, "sessionFactory");
        this.authorizer = Defense.notNull(authorizer, "authorizer");
    }

    @Override
    public <T> DAO<T, ?> build(Class<T> entityClass) {
        DAO<T, ?> dao = null;
        if (sessionFactory.getClassMetadata(entityClass) != null) {
            dao = new SecuredHibernateDAOImpl<T, Serializable>(entityClass, sessionFactory, authorizer);
        }
        return dao;
    }
}
