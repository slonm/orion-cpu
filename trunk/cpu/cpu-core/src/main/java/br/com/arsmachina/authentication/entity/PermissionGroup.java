package br.com.arsmachina.authentication.entity;

import java.util.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import orion.cpu.baseentities.NamedEntity;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Class that represents a group of permissions.
 * Класс представляет собой группу прав
 * @author Mihail Slobodyanuk
 */
@Entity
@Table(schema = "sec", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
public class PermissionGroup extends NamedEntity<PermissionGroup> {

    private static final long serialVersionUID = 1L;
    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     * No-arg constructor.
     */
    public PermissionGroup() {
    }

    /**
     * Constructor that receives a name.
     *
     * @param name a {@link String}. It cannot be null.
     * @param permission
     * @param permissions
     * @throws IllegalArgumentException if <code>name</code> is null.
     */
    public PermissionGroup(String name, Permission permission, Permission ... permissions) {
        setName(Defense.notNull(name, "name"));
        add(permission, permissions);
    }

    /**
     * Returns the value of the <code>permissions</code> property.
     * @return a {@link List<Permission>}.
     */
    @ManyToMany
    @JoinTable(schema = "sec", name = "permissiongroup_permission", joinColumns = @JoinColumn(name = "permissiongroup_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "permission_id", nullable = false))
    @Size(min = 1, max = 100)
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Adds a permission to this group.
     * @param permission a {@link Permission}.
     * @param permissions
     */
    public void add(Permission permission, Permission ... permissions) {
        this.permissions.add(permission);
        this.permissions.addAll(Arrays.asList(permissions));
    }

    /**
     * Removes a permission from this group.
     * @param permission a {@link Permission}.
     * @param permissions 
     */
    public void remove(Permission permission, Permission ... permissions) {
        this.permissions.remove(permission);
        this.permissions.removeAll(Arrays.asList(permissions));
    }

    /**
     * Changes the value of the <code>permissions</code> property.
     * Этот метод нужен только для Hibernate
     * @param permissions a {@link Set<Permission>}.
     * @deprecated Use {@link #add(Permission)} and {@link #remove(Permission)} instead.
     */
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Tells if this permission group has at least one of a set of permissions.
     * @param permissionNames an array of {@link String}s.
     * @return a <code>boolean</code>.
     */
    public boolean hasPermission(String... permissionNames) {
        for (Permission permission : permissions) {
            for (String permissionName : permissionNames) {
                if (permission.getName().equals(permissionName)) {
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
        for (Permission permission : permissions) {
            if (this.permissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean entityEquals(PermissionGroup obj) {
        return super.entityEquals(obj) &&
                aEqualsField(permissions, obj.getPermissions());
    }
}
