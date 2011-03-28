package br.com.arsmachina.authentication.dao.hibernate;

import br.com.arsmachina.authentication.dao.RoleDAO;
import br.com.arsmachina.authentication.entity.Role;
import org.hibernate.SessionFactory;

/**
 * Реализация DAO для ролей
 * @author sl
 */
public class RoleDAOImpl extends AbstractRoleDAOImpl<Role> implements
		RoleDAO{

    public RoleDAOImpl(SessionFactory sessionFactory) {
        super(Role.class, sessionFactory);
    }

    @Override
    protected void loadEverything(Role user) {
        // force the loading of the user's roles
        user.getUsers().size();
    }
}
