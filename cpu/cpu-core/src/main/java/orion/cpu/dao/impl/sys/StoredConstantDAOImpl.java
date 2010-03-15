package orion.cpu.dao.impl.sys;

import orion.cpu.dao.hibernate.*;
import org.hibernate.SessionFactory;
import orion.cpu.entities.sys.StoredConstant;
import orion.cpu.security.services.ExtendedAuthorizer;

/**
 *
 * @author sl
 */
public class StoredConstantDAOImpl extends HibernateNamedEntityDAO<StoredConstant>{

    public StoredConstantDAOImpl(SessionFactory sessionFactory, ExtendedAuthorizer authorizer) {
        super(StoredConstant.class, sessionFactory, authorizer);
    }

}
