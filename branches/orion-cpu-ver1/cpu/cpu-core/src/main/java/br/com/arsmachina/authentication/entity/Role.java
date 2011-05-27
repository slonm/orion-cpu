package br.com.arsmachina.authentication.entity;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import orion.cpu.baseentities.AbstractRole;
import orion.cpu.entities.sys.SubSystem;

/**
 * Class that represents a role an user can have in the application.
 * Класс представляет собой роль с которой работает пользователь в программе.
 * Каждая роль существует для определенной подсистемы. Другими словами
 * роль является гранью должностных обязанностей. Совокупность ролей и будет давать
 * все должностные обязанности.
 * @author Mihail Slobodyanuk
 */
@Entity
@Table(schema = "sec", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"login"})})
public class Role extends AbstractRole<Role> {

    private static final long serialVersionUID = 1L;
    private SubSystem subSystem;
    private Set<User> users = new HashSet<User>();

    public Role() {
    }

    public Role(String login, String name, SubSystem subSystem) {
        setLogin(login);
        setName(name);
        setSubSystem(subSystem);
    }
    /**
     * Отключаем ленивую загрузку из связанной таблицы, для исправления ошибки
     * failed to lazily initialize a collection of role: br.com.arsmachina.authentication.entity.Role.permissionGroups, 
     * no session or session was closed
     */
    @ManyToOne(fetch=FetchType.EAGER)
    @NotNull
    public SubSystem getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(SubSystem subSystem) {
        this.subSystem = subSystem;
    }

    /**
     * Adds a user to this role.
     * @param user a {@link User}.
     * @param users 
     */
    public void add(User user, User ... users) {
        this.users.add(user);
        this.users.addAll(Arrays.asList(users));
    }

    /**
     * Removes a user from this role.
     * @param user a {@link User}.
     * @param users 
     */
    public void remove(User user, User ... users) {
        this.users.remove(user);
        this.users.removeAll(Arrays.asList(users));
    }

    /**
     * Tells if some a given user has this role type.
     * @param user a {@link User} subclass.
     * @return a <code>boolean</code>.
     */
    public boolean hasUser(User user) {
        return users.contains(user);
    }

    /**
     * Returns the unmodifiable ordered value of the <code>users</code> property.
     * Отключаем ленивую загрузку из связанной таблицы, для исправления ошибки
     * failed to lazily initialize a collection of role: br.com.arsmachina.authentication.entity.Role.permissionGroups, 
     * no session or session was closed
     * @return a {@link Set<User>}.
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(schema = "sec", name = "user_role", joinColumns = 
    @JoinColumn(name = "role_id", nullable = false), inverseJoinColumns =
    @JoinColumn(name = "user_id", nullable = false))
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Changes the value of the <code>users</code> property.
     * Метод используется только для Hibernate
     * @param users a {@link Set<User>}.
     * @deprecated Use {@link #add(User)} and {@link #remove(User)} instead.
     */
    @Deprecated
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Returns unmodifiable ordered value of the <code>permissionGroups</code> property.
     * Отключаем ленивую загрузку из связанной таблицы, для исправления ошибки
     * failed to lazily initialize a collection of role: br.com.arsmachina.authentication.entity.Role.permissionGroups, 
     * no session or session was closed
     * @return a {@link Set<PermissionGroup>}.
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(schema = "sec", name = "role_permissiongroup", joinColumns = @JoinColumn(name = "role_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "permissiongroup_id", nullable = false))
    public Set<PermissionGroup> getPermissionGroups() {
        return permissionGroups();
    }

    @Override
    public String toString() {
        return (subSystem!=null?subSystem.getName()+". ":"")+getLogin()+"("+getName()+")";
    }

}
