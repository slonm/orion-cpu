package orion.cpu.services.impl;

import br.com.arsmachina.authentication.controller.*;
import br.com.arsmachina.authentication.entity.*;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.*;
import org.apache.tapestry5.ioc.annotations.Symbol;
import orion.cpu.services.CoreIOCModule;
import orion.cpu.services.StoredConstantsSource;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Помощник инициализатора базы данных
 * @author sl
 */
public class InitializeDatabaseSupport {

    private final StoredConstantsSource storedConstantsSource;
    private final ControllerSource controllerSource;
    private final PermissionController permissionController;
    private final PermissionGroupController permissionGroupController;
    private final RoleController roleController;
    private final UserController userController;
    private final boolean fillTestData;

    public InitializeDatabaseSupport(StoredConstantsSource storedConstantsSource,
            ControllerSource controllerSource, @Symbol(CoreIOCModule.FILL_TEST_DATA) boolean fillTestData) {
        this.storedConstantsSource = Defense.notNull(storedConstantsSource, "storedConstantsSource");
        this.controllerSource = Defense.notNull(controllerSource, "ControllerSource");
        this.permissionController = (PermissionController) (Object) controllerSource.get(Permission.class);
        this.permissionGroupController = (PermissionGroupController) (Object) controllerSource.get(PermissionGroup.class);
        this.roleController = (RoleController) (Object) controllerSource.get(Role.class);
        this.userController = (UserController) (Object) controllerSource.get(User.class);
        this.fillTestData = fillTestData;
    }

    public StoredConstantsSource getStoredConstantsSource() {
        return storedConstantsSource;
    }

    public ControllerSource getControllerSource() {
        return controllerSource;
    }

    public boolean isFillTestData() {
        return fillTestData;
    }

    public PermissionController getPermissionController() {
        return permissionController;
    }

    public PermissionGroupController getPermissionGroupController() {
        return permissionGroupController;
    }

    public RoleController getRoleController() {
        return roleController;
    }

    public UserController getUserController() {
        return userController;
    }

    public Map<String, Permission> getPermissionsMap(Class<?> subject, String type, String... types) {
        Defense.notNull(type, "type");
        Defense.notNull(subject, "subject");
        Map<String, Permission> map = new HashMap<String, Permission>();
        Permission prm = permissionController.findBySubjectAndType(subject, type);
        if (prm == null) {
            prm = new Permission(subject, type);
            permissionController.save(prm);
        }
        map.put(type, prm);
        for (String type1 : types) {
            prm = permissionController.findBySubjectAndType(subject, type1);
            if (prm == null) {
                prm = new Permission(subject, type1);
                permissionController.save(prm);
            }
            map.put(type1, prm);
        }
        return map;
    }

    public PermissionGroup saveOrUpdatePermissionGroup(String name, Permission permission, Permission... permissions) {
        PermissionGroup p = permissionGroupController.findByName(name);
        if (p == null) {
            p = new PermissionGroup(name, permission, permissions);
        } else {
            p.getPermissions().clear();
            p.add(permission, permissions);
        }
        return permissionGroupController.saveOrUpdate(p);
    }

    public Role saveOrUpdateRole(String login, String name, PermissionGroup group, PermissionGroup ... groups) {
        Role p = roleController.findByLogin(login);
        if (p == null) {
            p = new Role(login, name);
            p.add(group, groups);
        } else {
            p.getPermissionGroups().clear();
            p.getUsers().clear();
            p.setName(name);
            p.add(group, groups);
        }
        return roleController.saveOrUpdate(p);
    }
}
