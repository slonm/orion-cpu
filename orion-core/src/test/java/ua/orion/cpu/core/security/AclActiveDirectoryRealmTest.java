/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.security;

import ua.orion.cpu.core.security.services.ThreadRole;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ua.orion.cpu.core.security.entities.Acl;
import java.util.HashSet;
import java.util.Set;
import javax.naming.NamingException;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.authz.AuthorizationInfo;
import javax.persistence.EntityManager;
import org.hibernate.ejb.Ejb3Configuration;
import javax.persistence.EntityManagerFactory;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.apache.shiro.util.ThreadContext;
import ua.orion.cpu.core.security.entities.SubjectType;
import ua.orion.cpu.core.security.services.ThreadRoleImpl;

import static org.testng.Assert.*;

/**
 * 
 * @author sl
 */
public class AclActiveDirectoryRealmTest {

    AclActiveDirectoryRealm realm;
    ThreadRole thRole = new ThreadRoleImpl();

    @BeforeClass
    public void setup() {
        ThreadContext.remove();
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = cfg.buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                connection.setAutoCommit(true);
                //Удалим таблицу, если она была
                try {
                    Statement st = connection.createStatement();
                    st.execute("DROP TABLE sec.acl");
                } catch (Exception ex) {
                }
                //Создадим схему, если ее не было
                try {
                    Statement st = connection.createStatement();
                    st.execute("CREATE SCHEMA sec");
                } catch (Exception ex) {
                }
            }
        });
        session.close();
        sessionFactory.close();

        Ejb3Configuration configuration = new Ejb3Configuration();
        configuration.addAnnotatedClass(Acl.class);
        configuration.configure("hibernate.cfg.xml");
        EntityManagerFactory entityManagerFactory = configuration.buildEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        Acl acl = new Acl();
        acl.setSubjectType(SubjectType.USER);
        acl.setSubject("zav");
        acl.setPermission("Object1:edit");
        em.persist(acl);

        Acl acl1 = new Acl();
        acl1.setSubjectType(SubjectType.USER);
        acl1.setSubject("zav");
        acl1.setPermission("Object2:*");
        em.persist(acl1);

        Acl acl2 = new Acl();
        acl2.setSubjectType(SubjectType.ROLE);
        acl2.setSubject("developers");
        acl2.setPermission("Object3:read");
        em.persist(acl2);

        Acl acl3 = new Acl();
        acl3.setSubjectType(SubjectType.PERMISSION_GROUP);
        acl3.setSubject("Group1");
        acl3.setPermission("Object4:read");
        em.persist(acl3);

        Acl acl4 = new Acl();
        acl4.setSubjectType(SubjectType.USER);
        acl4.setSubject("zav");
        acl4.setPermission("permg_Group1");
        em.persist(acl4);

        Acl acl5 = new Acl();
        acl5.setSubjectType(SubjectType.PERMISSION_GROUP);
        acl5.setSubject("Group2");
        acl5.setPermission("Object5:read");
        em.persist(acl5);

        Acl acl6 = new Acl();
        acl6.setSubjectType(SubjectType.ROLE);
        acl6.setSubject("developers");
        acl6.setPermission("permg_Group2");
        em.persist(acl6);

        em.getTransaction().commit();
        //Обойдем обращение к AD
        realm = new AclActiveDirectoryRealm(em, thRole) {

            @Override
            protected AuthorizationInfo queryForAuthorizationInfo(PrincipalCollection principals, LdapContextFactory ldapContextFactory) throws NamingException {
                Set<String> roleNames = new HashSet();
                roleNames.add("developers");
                return buildAuthorizationInfo(roleNames, "zav");
            }
        };
    }

    @AfterClass
    public void tearDown() {
    }

    @Test
    public void testBuildAuthorizationInfo() {
        PrincipalCollection principals = new SimplePrincipalCollection("zav", "Dummy");
        assertTrue(realm.isPermitted(principals, "Object1:edit"));//User permission
        assertTrue(realm.isPermitted(principals, "object1:edit"));//Case insensitive
        assertTrue(realm.isPermitted(principals, "Object2:read"));//User wildcard permission
        assertFalse(realm.isPermitted(principals, "Object3:read"));//Role permission
        assertTrue(realm.isPermitted(principals, "Object4:read"));//PERMISSION_GROUP permission for USER
        assertFalse(realm.isPermitted(principals, "Object5:read"));//PERMISSION_GROUP permission for ROLE
        thRole.setRole("developers");
        assertTrue(realm.isPermitted(principals, "Object3:read"));//Role permission
        assertTrue(realm.isPermitted(principals, "Object5:read"));//PERMISSION_GROUP permission for ROLE
    }
}
