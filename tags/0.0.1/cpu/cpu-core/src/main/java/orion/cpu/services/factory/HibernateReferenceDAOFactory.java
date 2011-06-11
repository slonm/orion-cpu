package orion.cpu.services.factory;

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.module.factory.DAOFactory;
import org.hibernate.SessionFactory;
import orion.cpu.dao.hibernate.HibernateReferenceDAO;
import orion.cpu.baseentities.ReferenceEntity;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Фабрика создает объект HibernateReferenceDAO
 * @author sl
 */
public class HibernateReferenceDAOFactory implements DAOFactory {

    final private SessionFactory sessionFactory;
    /**
     * Single constructor of this class.
     *
     * @param sessionFactory a {@link SessionFactory}. It cannot be null.
     * @param authorizer 
     */
    public HibernateReferenceDAOFactory(SessionFactory sessionFactory) {
        this.sessionFactory = Defense.notNull(sessionFactory, "sessionFactory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> DAO<T, ?> build(Class<T> entityClass) {
        DAO<T, ?> dao = null;
        if (sessionFactory.getClassMetadata(entityClass) != null &&
                ReferenceEntity.class.isAssignableFrom(entityClass)) {
            dao = new HibernateReferenceDAO(entityClass, sessionFactory);
        }
        return dao;
    }
}
