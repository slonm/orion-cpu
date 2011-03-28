package orion.cpu.services.factory;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.factory.DAOFactory;
import orion.cpu.dao.hibernate.OrionHibernateGenericDAO;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link DefaultDAOFactory} implementation using Hibernate.
 * 
 * @author sl
 */
public class OrionHibernateGenericDAOFactory implements DAOFactory {

    final private SessionFactory sessionFactory;

    /**
     * Single constructor of this class.
     * 
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     */
    public OrionHibernateGenericDAOFactory(SessionFactory sessionFactory) {
        this.sessionFactory = Defense.notNull(sessionFactory, "sessionFactory");
    }

    @Override
    public <T> DAO<T, ?> build(Class<T> entityClass) {
        DAO<T, ?> dao = null;
        if (sessionFactory.getClassMetadata(entityClass) != null) {
            dao = new OrionHibernateGenericDAO<T, Serializable>(entityClass, sessionFactory);
        }
        return dao;
    }
}
