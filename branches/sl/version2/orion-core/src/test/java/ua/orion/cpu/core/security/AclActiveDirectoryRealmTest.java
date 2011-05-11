/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.cpu.core.security;

import ua.orion.cpu.core.security.AclActiveDirectoryRealm;
import ua.orion.cpu.core.security.entities.Acl;
import javax.persistence.EntityManager;
import org.hibernate.ejb.Ejb3Configuration;
import javax.persistence.EntityManagerFactory;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import ua.orion.cpu.core.security.entities.SubjectType;

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
        Ejb3Configuration configuration = new Ejb3Configuration();
        configuration.addAnnotatedClass(Acl.class);
        configuration.configure("hibernate.cfg.xml");
        EntityManagerFactory entityManagerFactory = configuration.buildEntityManagerFactory();
        EntityManager em=entityManagerFactory.createEntityManager();
        
        em.getTransaction().begin();
        Acl acl=new Acl();
        acl.setSubjectType(SubjectType.USER);
        acl.setSubject("zav");
        acl.setPermission("Object1:edit");
        em.persist(acl);

        Acl acl1=new Acl();
        acl1.setSubjectType(SubjectType.USER);
        acl1.setSubject("zav");
        acl1.setPermission("Object2:*");
        em.persist(acl1);
        
        Acl acl2=new Acl();
        acl2.setSubjectType(SubjectType.ROLE);
        acl2.setSubject("developers");
        acl2.setPermission("Object3:read");
        em.persist(acl2);
        em.getTransaction().commit();
        
        realm = new AclActiveDirectoryRealm(em);
        realm.setSystemUsername("student");
        realm.setSystemPassword("student");
        realm.setUrl("ldap://localhost:389");
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
    public void testPermission() {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("zav", "zav"));
        assertTrue(realm.isPermitted( subject.getPrincipals(), "Object1:edit" ));//User permission
        assertTrue(realm.isPermitted( subject.getPrincipals(), "object1:edit" ));//Case insensitive
        assertTrue(realm.isPermitted( subject.getPrincipals(), "Object2:read" ));//User wildcard permission
        assertTrue(realm.isPermitted( subject.getPrincipals(), "Object3:read" ));//Role permission

        subject.logout();
    }
}
