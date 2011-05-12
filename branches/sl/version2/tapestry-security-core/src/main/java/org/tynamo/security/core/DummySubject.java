/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tynamo.security.core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author slobodyanuk
 */
public class DummySubject implements Subject {

    @Override
    public Object getPrincipal() {
        return "Dummy";
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection("Dummy","Dummy");
    }

    @Override
    public boolean isPermitted(String permission) {
        return true;
    }

    @Override
    public boolean isPermitted(Permission permission) {
        return true;
    }

    @Override
    public boolean[] isPermitted(String... permissions) {
        boolean[] ret=new boolean[permissions.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=true;
        }
        return ret;
    }

    @Override
    public boolean[] isPermitted(List<Permission> permissions) {
        boolean[] ret=new boolean[permissions.size()];
        for(int i=0;i<ret.length;i++){
            ret[i]=true;
        }
        return ret;
    }

    @Override
    public boolean isPermittedAll(String... permissions) {
        return true;
    }

    @Override
    public boolean isPermittedAll(Collection<Permission> permissions) {
        return true;
    }

    @Override
    public void checkPermission(String permission) throws AuthorizationException {
    }

    @Override
    public void checkPermission(Permission permission) throws AuthorizationException {
    }

    @Override
    public void checkPermissions(String... permissions) throws AuthorizationException {
    }

    @Override
    public void checkPermissions(Collection<Permission> permissions) throws AuthorizationException {
    }

    @Override
    public boolean hasRole(String roleIdentifier) {
        return true;
    }

    @Override
    public boolean[] hasRoles(List<String> roleIdentifiers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasAllRoles(Collection<String> roleIdentifiers) {
        return true;
    }

    @Override
    public void checkRole(String roleIdentifier) throws AuthorizationException {
    }

    @Override
    public void checkRoles(Collection<String> roleIdentifiers) throws AuthorizationException {
    }

    @Override
    public void checkRoles(String... roleIdentifiers) throws AuthorizationException {
    }

    @Override
    public void login(AuthenticationToken token) throws AuthenticationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public boolean isRemembered() {
        return true;
    }

    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public Session getSession(boolean create) {
        return null;
    }

    @Override
    public void logout() {
    }

    @Override
    public <V> V execute(Callable<V> callable) throws ExecutionException {
        return null;
    }

    @Override
    public void execute(Runnable runnable) {
    }

    @Override
    public <V> Callable<V> associateWith(Callable<V> callable) {
        return null;
    }

    @Override
    public Runnable associateWith(Runnable runnable) {
        return null;
    }

    @Override
    public void runAs(PrincipalCollection principals) throws NullPointerException, IllegalStateException {
    }

    @Override
    public boolean isRunAs() {
        return false;
    }

    @Override
    public PrincipalCollection getPreviousPrincipals() {
        return null;
    }

    @Override
    public PrincipalCollection releaseRunAs() {
        return null;
    }
    
}
