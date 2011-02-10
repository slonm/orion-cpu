/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.cpu.security;

import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import ua.orion.cpu.security.entities.ActiveDirectoryPrincipal;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import java.util.*;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import static org.testng.Assert.assertTrue;
/**
 *
 * @author sl
 */
public class AclActiveDirectoryRealmTest {
    

    DefaultSecurityManager securityManager = null;
    AclActiveDirectoryRealm realm;

    @BeforeClass
    public void setup() {
        ThreadContext.remove();
        realm = new AclActiveDirectoryRealm();
        realm.setSystemUsername("student");
        realm.setSystemPassword("student");
        realm.setSearchBase("DC=uni,DC=zhu,DC=edu,DC=ua");
        realm.setUrl("ldap://172.16.1.2:389");
        realm.setPrincipalSuffix("@uni.zhu.edu.ua");
        securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);
    }

    @AfterClass
    public void tearDown() {
        SecurityUtils.setSecurityManager(null);
        securityManager.destroy();
        ThreadContext.remove();
    }

    @Test
    public void testDefaultConfig() {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("zav", "zav"));
        assertTrue(realm.isPermitted( subject.getPrincipals(), "Object1:edit" ));//User permission
        assertTrue(realm.isPermitted( subject.getPrincipals(), "Object2:read" ));//Role permission

        subject.logout();
    }
}
