package orion.cpu.security.services.impl;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.authorization.*;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.Stack;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.hibernate.Session;
import orion.cpu.security.OperationTypes;
import orion.cpu.security.exceptions.*;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Сервис авторизации
 * @author sl
 */
//TODO Убрать все обращения к БД
@Scope(ScopeConstants.PERTHREAD)
public class ExtendedAuthorizerImpl implements ExtendedAuthorizer {

    private final ControllerSource controllerSource;
    private User user;
    private Role role;
    private Stack<User> userStack = new Stack<User>();
    private Stack<Role> roleStack = new Stack<Role>();
    private Session session;

    /**
     * Single constructor of this class.
     *
     * @param applicationStateManager
     * @param controllerSource
     */
    @SuppressWarnings("unchecked")
    public ExtendedAuthorizerImpl(ControllerSource controllerSource, Session session) {
        this.controllerSource = Defense.notNull(controllerSource, "controllerSource");
        this.session = Defense.notNull(session, "session");
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
        return can(new Permission(clasz, OperationTypes.STORE_OP), null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canUpdate(Class<?> clasz) {
        return can(new Permission(clasz, OperationTypes.UPDATE_OP), null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canUpdate(Object object) {
        Defense.notNull(object, "object");
        return can(new Permission(object.getClass(), OperationTypes.UPDATE_OP), object);
    }

    @Override
    public boolean canRemove(Class<?> clasz) {
        return can(new Permission(clasz, OperationTypes.REMOVE_OP), null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canRemove(Object object) {
        Defense.notNull(object, "object");
        return can(new Permission(object.getClass(), OperationTypes.REMOVE_OP), object);
    }

    @Override
    public boolean canSearch(Class<?> clasz) {
        return can(new Permission(clasz, OperationTypes.READ_OP), null);
    }

    @Override
    public boolean canRead(Class<?> clasz) {
        return can(new Permission(clasz, OperationTypes.READ_OP), null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canRead(Object object) {
        Defense.notNull(object, "object");
        return can(new Permission(object.getClass(), OperationTypes.READ_OP), object);
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

//FIXME рассматриваются только аннотации!
    private boolean canReadBySchema(Class<?> subject) {
        javax.persistence.Table t = subject.getAnnotation(javax.persistence.Table.class);
        if (t == null) {
            return false;
        }
        if ("sys".equals(t.schema()) || "sec".equals(t.schema()) || "ref".equals(t.schema())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean can(Permission permission) {
        return can(permission, null);
    }

    @Override
    public boolean can(Permission permission, Object object) {
        Defense.notNull(permission, "permission");
        /*Добавлена проверка связаны ли текущие объекты user и role с сессией
         * если сессия закрыта, то !session.contains(user) возвращает true
         * и выполняется повторное открытие сессии для этого объекта т предотвращается
         * ошибка failed to lazily initialize a collection of role: 
         * br.com.arsmachina.authentication.entity.Role.permissionGroups, no session or session was closed
         */
        if (user != null && user.getId()!=null && !session.contains(user)) {
            user = getUserController().findById(user.getId());
        }
        if (role != null && role.getId()!=null && !session.contains(role)) {
            role = getRoleController().findById(role.getId());
        }
        //Конец проверки
        
        //rule: by schema in (sys, sec, ref)
        if (permission.getPermissionType().equals(OperationTypes.READ_OP)
                && canReadBySchema(permission.getSubject())) {
            return true;
        }
        //rule: by SYSTEM_USER
        if (user != null && User.SYSTEM_USER.getLogin().equals(user.getLogin())) {
            return true;
        }
        //подготовка к проверке по ACL
        //если permission не persistent то извлечем его из базы
        if (!getPermissionController().isPersistent(permission)) {
            Permission persistentPermission = getPermissionController().
                    findBySubjectAndType(permission.getSubject(), permission.getPermissionType());
            if (persistentPermission != null) {
                permission = persistentPermission;
            }
        }
        //rule: by user ACL
        if (user != null && user.hasPermission(permission)) {
            return true;
        }
        //rule: by role ACL
        if (role != null && role.hasPermission(permission)) {
            return true;
        }
        //rule: by subject parent
        Class<?> s = permission.getSubject().getSuperclass();
        if (!s.equals(Object.class)) {
            return can(new Permission(s, permission.getPermissionType()), object);
        }
        //rule: default
        return false;
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

    @Override
    public void popUserAndRole() {
        user = userStack.pop();
        role = roleStack.pop();
    }

    @Override
    public void pushUserAndRole() {
        userStack.push(user);
        roleStack.push(role);
        user = null;
        role = null;
    }
}
