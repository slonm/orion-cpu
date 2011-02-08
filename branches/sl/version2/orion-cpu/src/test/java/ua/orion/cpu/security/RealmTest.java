/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ua.orion.cpu.security;

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
 * Simple test case for ActiveDirectoryRealm.
 * <p/>
 * todo:  While the original incarnation of this test case does not actually test the
 * heart of ActiveDirectoryRealm (no meaningful implemenation of queryForLdapAccount, etc) it obviously should.
 * This version was intended to mimic my current usage scenario in an effort to debug upgrade issues which were not related
 * to LDAP connectivity.
 *
 */
public class RealmTest {

    DefaultSecurityManager securityManager = null;
    OrionActiveDirectoryRealm realm;

    @BeforeClass
    public void setup() {
        ThreadContext.remove();
        realm = new OrionActiveDirectoryRealm();
        realm.setSystemUsername("student");
        realm.setSystemPassword("student");
        realm.setPermissionResolver(null);
        realm.setRolePermissionResolver(null);
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
        assertTrue(subject.isAuthenticated());
        assertTrue(subject.hasRole("Developers"));//Primary Group
        assertTrue(subject.hasRole("Builtin.Users"));
//        assertTrue( realm.isPermitted( pCollection, ROLE + ":perm1" ) );
//        assertTrue( realm.isPermitted( pCollection, ROLE + ":perm2" ) );
//        assertFalse( realm.isPermitted( pCollection, ROLE + ":perm3" ) );
//        assertTrue( realm.isPermitted( pCollection, "other:bar:foo" ) );



        ActiveDirectoryPrincipal usernamePrincipal = subject.getPrincipals().oneByType(ActiveDirectoryPrincipal.class);
        assertTrue("zav".equals(usernamePrincipal.getLogin()));
        assertTrue("S-1-5-21-3809343169-324925516-669149220-2312".equals(usernamePrincipal.getSid()));
        assertTrue("Зинченко Антон Владимирович".equals(usernamePrincipal.getFio()));
        
        subject.logout();
    }

    static class ACLRolePermissionResolverTester implements RolePermissionResolver {

        @Override
        public Collection<Permission> resolvePermissionsInRole(String roleString) {
            Collection<Permission> permissions = new HashSet<Permission>();
            if ("students".equals(roleString)) {
                permissions.add(new WildcardPermission("students:perm1"));
                permissions.add(new WildcardPermission("students:edit:User"));
                permissions.add(new WildcardPermission("other:*:foo"));
            }
            return permissions;
        }
    }
}