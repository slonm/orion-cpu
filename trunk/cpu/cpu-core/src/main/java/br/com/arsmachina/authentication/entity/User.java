package br.com.arsmachina.authentication.entity;

import java.util.*;
import javax.persistence.*;
import org.hibernate.validator.*;
import orion.cpu.baseentities.AbstractRole;

/**
 * Class that represents an application user.
 * Класс представляет собой пользователя программы.
 * @author Mihail Slobodyanuk
 */
@Entity
@Table(schema = "sec", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"login"})})
public class User extends AbstractRole<User> {

    /**
     * Пользователь - система
     */
    public final static User SYSTEM_USER;

    private static final long serialVersionUID = 1L;
    private String email;
    private Boolean credentialsExpired = false;
    private Boolean expired = false;
    private Boolean locked = false;
    private Boolean loggedIn = false;
    private String password;
    private Set<Role> roles = new HashSet<Role>();

    static {
    SYSTEM_USER = new User("SYSTEM", "******", "SYSTEM", "SYS@SYS.SYS");
    SYSTEM_USER.setEnabled(false);
    }

    public User() {
    }

    public User(String login, String password, String name, String email) {
        setLogin(login);
        setPassword(password);
        setName(name);
        setEmail(email);
    }

    /**
     * Adds a role to this user.
     * @param role a {@link Role}.
     * @param roles
     */
    public void add(Role role, Role... roles) {
        this.roles.add(role);
        this.roles.addAll(Arrays.asList(roles));
    }

    /**
     * Removes a role from this user.
     * @param role a {@link Role}.
     * @param roles 
     */
    public void remove(Role role, Role... roles) {
        this.roles.remove(role);
        this.roles.removeAll(Arrays.asList(roles));
    }

    /**
     * Tells if this user has some a given role type.
     * @param role a {@link Role} subclass.
     * @return a <code>boolean</code>.
     */
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    /**
     * Returns the unmodifiable ordered value of the <code>roles</code> property.
     * @return a {@link Set<Role>}.
     */
    @ManyToMany
    @JoinTable(schema = "sec", name = "user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Returns the value of the <code>email</code> property.
     * @return a {@link String}.
     */
    @Email
    @Length(min = 5, max = 50)
    public String getEmail() {
        return email;
    }

    /**
     * Returns the value of the <code>password</code> property.
     * @return a {@link String}.
     */
    @Column(nullable = false, length = 40)
    @NotNull
    @Length(min = 6, max = 40)
    public String getPassword() {
        return password;
    }

    /**
     * Is this user's credentials expired?
     * @return a {@link Boolean}.
     */
    public Boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    /**
     * Is this user's account expired?
     * @return a {@link Boolean}.
     */
    public Boolean isExpired() {
        return expired;
    }

    /**
     * Is this user's account locked?
     * @return a {@link Boolean}.
     */
    public Boolean isLocked() {
        return locked;
    }

    /**
     * Is this user's logged in now?
     * @return a {@link Boolean}.
     */
    public Boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Changes the value of the <code>credentialsExpired</code> property.
     * @param credentialsExpired a {@link Boolean}.
     */
    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    /**
     * Changes the value of the <code>email</code> property.
     * @param email a {@link String}.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Changes the value of the <code>expired</code> property.
     * @param expired a {@link Boolean}.
     */
    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    /**
     * Changes the value of the <code>locked</code> property.
     * @param locked a {@link Boolean}.
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * Changes the value of the <code>loggedIn</code> property.
     * @param loggedIn a {@link Boolean}.
     */
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Changes the value of the <code>password</code> property.
     * @param password a {@link String}.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Changes the value of the <code>roles</code> property.
     * @param roles a {@link Set<Role>}.
     * @deprecated Use {@link #add(Role)} and {@link #remove(Role)} instead.
     */
    @Deprecated
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Returns the <code>name</code> property.
     * @return a {@link String}.
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns unmodifiable ordered value of the <code>permissionGroups</code> property.
     * @return a {@link Set<PermissionGroup>}.
     */
    @ManyToMany
    @JoinTable(schema = "sec", name = "user_permissiongroup", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "permissiongroup_id", nullable = false))
    //FIXME разобратся как переопределять @JoinTable и заменить эти костыли на нормальное объявление
    public Set<PermissionGroup> getPermissionGroups() {
        return super.permissionGroups();
    }
}
