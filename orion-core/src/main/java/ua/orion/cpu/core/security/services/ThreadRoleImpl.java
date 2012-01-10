/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.security.services;

import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;

/**
 *
 * @author slobodyanuk
 */
@Scope(ScopeConstants.PERTHREAD)
public class ThreadRoleImpl implements ThreadRole{

    private String role = null;

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        if ("".equals(role)) {
            role = null;
        }
        this.role = role;
    }
}
