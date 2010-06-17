package orion.cpu.security.services.impl;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.authorization.*;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.List;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.hibernate.Criteria;
import orion.cpu.security.OperationTypes;
import orion.cpu.security.exceptions.*;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Сервис авторизации
 * @author sl
 */
@Scope(ScopeConstants.PERTHREAD)
public class ExtendedAuthorizerImpl implements ExtendedAuthorizer {

    private final ControllerSource controllerSource;
    private User user;
    private Role role;

    /**
     * Single constructor of this class.
     *
     * @param applicationStateManager
     * @param controllerSource
     */
    @SuppressWarnings("unchecked")
    public ExtendedAuthorizerImpl(ControllerSource controllerSource) {
        this.controllerSource = Defense.notNull(controllerSource, "controllerSource");
    }

    private PermissionController getPermissionController() {
        return (PermissionController) (Object) controllerSource.get(Permission.class);
    }

    private UserController getUserController() {
        return (UserController) (Object) controllerSource.get(User.class);
    }

    private RoleController getRoleController() {
        return (RoleController) (Object) controllerSource.get(Role.class);
    }

    @Override
    public boolean canStore(Class<?> clasz) {
        return can(getPermissionController().findBySubjectAndType(clasz, OperationTypes.STORE_OP));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canUpdate(Class<?> clasz) {
        return can(getPermissionController().findBySubjectAndType(clasz, OperationTypes.UPDATE_OP));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canUpdate(Object object) {
        Defense.notNull(object, "object");
        return can(getPermissionController().findBySubjectAndType(object.getClass(), OperationTypes.UPDATE_OP), object);
    }

    @Override
    public boolean canRemove(Class<?> clasz) {
        return can(getPermissionController().findBySubjectAndType(clasz, OperationTypes.REMOVE_OP));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canRemove(Object object) {
        Defense.notNull(object, "object");
        return can(getPermissionController().findBySubjectAndType(object.getClass(), OperationTypes.REMOVE_OP), object);
    }

    @Override
    public boolean canSearch(Class<?> clasz) {
        return can(getPermissionController().findBySubjectAndType(clasz, OperationTypes.READ_OP));
    }

    @Override
    public boolean canRead(Class<?> clasz) {
        return can(getPermissionController().findBySubjectAndType(clasz, OperationTypes.READ_OP));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canRead(Object object) {
        Defense.notNull(object, "object");
        return can(getPermissionController().findBySubjectAndType(object.getClass(), OperationTypes.READ_OP), object);
    }

    @Override
    public void checkRead(Class<?> clasz) {
        if (canRead(clasz) == false) {
            throw new ReadTypeAuthorizationException(clasz);
        }
    }

    @Override
    public void checkRead(Object object) {
        if (canRead(object) == false) {
            throw new ReadObjectAuthorizationException(object);
        }
    }

    @Override
    public void checkRemove(Class<?> clasz) {
        if (canRemove(clasz) == false) {
            throw new RemoveTypeAuthorizationException(clasz);
        }
    }

    @Override
    public void checkRemove(Object object) {
        if (canRemove(object) == false) {
            throw new RemoveObjectAuthorizationException(object);
        }
    }

    @Override
    public void checkSearch(Class<?> clasz) {
        if (canSearch(clasz) == false) {
            throw new SearchTypeAuthorizationException(clasz);
        }
    }

    @Override
    public void checkStore(Class<?> clasz) {
        if (canStore(clasz) == false) {
            throw new StoreTypeAuthorizationException(clasz);
        }
    }

    @Override
    public void checkUpdate(Class<?> clasz) {
        if (canUpdate(clasz) == false) {
            throw new UpdateTypeAuthorizationException(clasz);
        }
    }

    @Override
    public void checkUpdate(Object object) {
        if (canUpdate(object) == false) {
            throw new UpdateObjectAuthorizationException(object);
        }
    }

    //TODO Row Level Security
    @Override
    public void addConstraintsToCriteria(Criteria criteria) {
    }

    @Override
    public boolean can(Permission permission) {
        if (permission == null) {
            return false;
        }
        if (user != null) {
            if (User.SYSTEM_USER.getLogin().equals(user.getLogin())) {
                return true;
            }
            if (role != null) {
                return user.hasPermission(permission) || role.hasPermission(permission);
            } else {
                return user.hasPermission(permission);
            }
        }
        return false;
    }

    //TODO Row Level Security
    @Override
    public boolean can(Permission permission, Object object) {
        if (User.SYSTEM_USER.getLogin().equals(user.getLogin())) {
            return true;
        }
        return can(permission);
    }

    @Override
    public void check(Permission permission) {
        Defense.notNull(permission, "permission");
        if (permission.getPermissionType().equals(OperationTypes.READ_OP)) {
            checkRead(permission.getSubject());
        } else if (permission.getPermissionType().equals(OperationTypes.REMOVE_OP)) {
            checkRemove(permission.getSubject());
        } else if (permission.getPermissionType().equals(OperationTypes.STORE_OP)) {
            checkStore(permission.getSubject());
        } else if (permission.getPermissionType().equals(OperationTypes.UPDATE_OP)) {
            checkUpdate(permission.getSubject());
        } else {
            if (can(permission) == false) {
                throw new AdditionalTypeAuthorizationException(permission);
            }
        }
    }

    //TODO Row Level Security
    @Override
    public void check(Permission permission, Object object) {
        Defense.notNull(permission, "permission");
        if (permission.getPermissionType().equals(OperationTypes.READ_OP)) {
            checkRead(object);
        } else if (permission.getPermissionType().equals(OperationTypes.REMOVE_OP)) {
            checkRemove(object);
        } else if (permission.getPermissionType().equals(OperationTypes.UPDATE_OP)) {
            checkUpdate(object);
        } else {
            if (can(permission) == false) {
                throw new AdditionalObjectAuthorizationException(permission, object);
            }
        }
    }

    @Override
    public List<Class<?>> listPermitted(String permissionType) {
        List<Class<?>> lst = getUserController().findPermittedTypes(user, permissionType);
        lst.addAll(getRoleController().findPermittedTypes(role, permissionType));
        return lst;
    }

    @Override
    public Role storeUserAndRole(User user, Role role) {
        if (role != null) {
            if (user == null || !user.hasRole(role)) {
                role = null;
            }
        }
        this.user = user;
        this.role = role;
        return role;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public User getUser() {
        return user;
    }
}
