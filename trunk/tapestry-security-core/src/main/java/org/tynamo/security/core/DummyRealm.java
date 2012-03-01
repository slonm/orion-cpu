/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tynamo.security.core;

import java.util.Collection;
import java.util.List;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Realm - заглушка. Аутентифицирует любого пользователя и разрешает все
 *
 * @author slobodyanuk
 */
public class DummyRealm implements Realm {

    public DummyRealm() {
    }

    @Override
    public String getName() {
        return "Dummy";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return true;
    }

    @Override
    public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {
        return true;
    }

    static private boolean[] trueVector(int length) {
        boolean[] ret = new boolean[length];
        for (int i = 0; i < length; i++) {
            ret[i] = true;
        }
        return ret;
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, String... permissions) {
        if (permissions != null) {
            return trueVector(permissions.length);
        } else {
            return new boolean[0];
        }
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, List<Permission> permissions) {
        if (permissions != null) {
            return trueVector(permissions.size());
        } else {
            return new boolean[0];
        }
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, String... permissions) {
        return true;
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) {
        return true;
    }

    @Override
    public void checkPermission(PrincipalCollection subjectPrincipal, String permission) throws AuthorizationException {
    }

    @Override
    public void checkPermission(PrincipalCollection subjectPrincipal, Permission permission) throws AuthorizationException {
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectPrincipal, String... permissions) throws AuthorizationException {
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) throws AuthorizationException {
    }

    @Override
    public boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {
        return true;
    }

    @Override
    public boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers) {
        if (roleIdentifiers != null) {
            return trueVector(roleIdentifiers.size());
        } else {
            return new boolean[0];
        }
    }

    @Override
    public boolean hasAllRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) {
        return true;
    }

    @Override
    public void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier) throws AuthorizationException {
    }

    @Override
    public void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) throws AuthorizationException {
    }

    @Override
    public void checkRoles(PrincipalCollection subjectPrincipal, String... roleIdentifiers) throws AuthorizationException {
    }
}
