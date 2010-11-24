package orion.cpu.baseentities;

import br.com.arsmachina.authentication.entity.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Class that represents a role an user can have in the application.
 * Класс представляет собой абстрактную роль. Является базовым классом для ролей и пользователей
 * Так как если пользователи будут наследоватся от ролей, то появится возможность влючать
 * пользователя в список ролей другого пользователя
 * @param <T> конечный класс
 * @author Mihail Slobodyanuk
 */
@MappedSuperclass
public abstract class AbstractRole<T extends AbstractRole<?>> extends NamedEntity<T> {

    private static final long serialVersionUID = 1L;
    private Boolean enabled = true;
    private String login;
    private Set<PermissionGroup> permissionGroups = new HashSet<PermissionGroup>();

    /**
     * Adds a permission group to this role.
     * @param permissionGroup a {@link PermissionGroup}.
     * @param permissionGroups
     */
    public void add(PermissionGroup permissionGroup, PermissionGroup ... permissionGroups) {
        this.permissionGroups.add(permissionGroup);
        this.permissionGroups.addAll(Arrays.asList(permissionGroups));
    }

    /**
     * Returns unmodifiable ordered value of the <code>permissionGroups</code> property.
     * @return a {@link Set<PermissionGroup>}.
     */
    public Set<PermissionGroup> permissionGroups() {
        return permissionGroups;
    }

    /**
     * Returns an unmodifiable ordered set containing all the permissions granted to this role. It is
     * comprised by the sum of all permissions in its permission groups.
     * @return a {@link Set} of {@link Permission}s.
     */
    @Transient
    public Set<Permission> getPermissions() {
        Set<Permission> permissions = new TreeSet<Permission>();
        for (PermissionGroup group : permissionGroups()) {
            for (Permission permission : group.getPermissions()) {
                permissions.add(permission);
            }
        }
        return Collections.unmodifiableSet(permissions);
    }

    /**
     * Changes the value of the <code>permissionGroups</code> property.
     * Только для Hibernate
     * @param permissionGroups a {@link List<PermissionGroup>}.
     * @deprecated Use {@link #add(PermissionGroup)} and {@link #remove(PermissionGroup)} instead.
     */
    @Deprecated
    public void setPermissionGroups(Set<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
    }

    /**
     * Removes a permission group from this user.
     * @param permissionGroup a {@link PermissionGroup}.
     * @param permissionGroups 
     */
    public void remove(PermissionGroup permissionGroup, PermissionGroup ... permissionGroups) {
        this.permissionGroups.remove(permissionGroup);
        this.permissionGroups.removeAll(Arrays.asList(permissionGroups));
    }

    /**
     * Is this user's account enabled?.
     * @return a {@link Boolean}.
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * Changes the value of the <code>enabled</code> property.
     * @param enabled a {@link Boolean}.
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the value of the <code>login</code> property.
     * @return a {@link String}.
     */
    @Column(nullable = false, unique = true)
    @NotNull
    @Size(min = 2, max = 50)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login.toLowerCase();
    }

    /**
     * Tells if this user has at least one of a set of permissions.
     * @param permissionNames an array of {@link String}s.
     * @return a <code>boolean</code>.
     */
    public boolean hasPermission(String... permissionNames) {
        if (enabled) {
            for (PermissionGroup permissionGroup : permissionGroups()) {
                if (permissionGroup.hasPermission(permissionNames)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tells if this user has at least one of a set of permissions.
     * @param permissions an array of {@link Permission}s.
     * @return a <code>boolean</code>.
     */
    public boolean hasPermission(Permission... permissions) {
        if (enabled) {
            for (PermissionGroup permissionGroup : permissionGroups()) {
                if (permissionGroup.hasPermission(permissions)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean entityEquals(T obj) {
        return super.entityEquals(obj) &&
                aEqualsField(login, obj.getLogin());
    }
}
