package br.com.arsmachina.authentication.controller.impl;

import java.util.Random;
import br.com.arsmachina.authentication.controller.PasswordEncrypter;
import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.dao.UserDAO;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.module.service.DAOSource;
import orion.cpu.controllers.event.AfterFindByLoginAndPasswordEv;
import orion.cpu.controllers.listenersupport.AbortControllerEventException;
import orion.cpu.services.DefaultControllerListeners;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link UserController} implementation.
 * 
 * @author sl
 */
public class UserControllerImpl extends AbstractRoleControllerImpl<User>
        implements UserController {

    private Random random = new Random();
    private UserDAO dao;
    private PasswordEncrypter passwordEncrypter;

    /**
     * Single constructor of this class.
     *
     * @param daoSource an {@link DAOSource}. It cannot be <code>null</code>.
     * @param passwordEncrypter a {@link PasswordEncrypter}. It cannot be
     *            <code>null</code>.
     * @param defaultControllerListeners
     */
    public UserControllerImpl(DAOSource daoSource, PasswordEncrypter passwordEncrypter,
                DefaultControllerListeners defaultControllerListeners) {
        super(User.class, daoSource, defaultControllerListeners);
        this.dao = (UserDAO) (Object) daoSource.get(User.class);
        this.passwordEncrypter = Defense.notNull(passwordEncrypter, "passwordEncrypter");
    }

    @Override
    public void save(User user) {
        setPasswordIfNeeded(user);
        encryptPassword(user);
        super.save(user);
    }

    @Override
    public User update(User user) {
        encryptPassword(user);
        return super.update(user);
    }

    private void encryptPassword(User user) {
        String password = user.getPassword();
        password = passwordEncrypter.encrypt(password);
        user.setPassword(password);
    }

    /**
     * Creates a temporary throw-away random password if it was not set yet.
     * @param user an {@link User}.
     */
    private void setPasswordIfNeeded(User user) {
        if (user.getPassword() == null) {
            user.setPassword(Integer.toHexString(random.nextInt()));
        }
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        try {
            User l = dao.findByLoginAndPassword(login, password);
            return processAfterEvent(new AfterFindByLoginAndPasswordEv<User>(l, login, password));
        } catch (AbortControllerEventException ex) {
            return null;
        }
    }
}
