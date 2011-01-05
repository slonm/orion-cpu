/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import test.foo.entities.*;
/**
 *
 * @author user
 */
public class TestCascade {
    @Test
    public void setUp() throws Exception {
        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.addAnnotatedClass(Prikaz.class);
        cfg.addAnnotatedClass(Command.class);
        cfg.configure("hibernate.cfg.xml");
        EntityManagerFactory emf = cfg.buildEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Prikaz order = new Prikaz();
        order.setNumber("123");
        Command com=new Command();
        com.setBody("command");
        //order.getCommands().add(com);
        com.setPrikaz(order);
        em.persist(com);
        em.persist(order);
        assertTrue(em.contains(com));
        em.getTransaction().commit();
        em.detach(order);
        em.detach(com);
        Prikaz prikaz=em.find(Prikaz.class, 1L);
        //Command com1=em.find(Command.class, 1L);
        assertEquals(prikaz.getCommands().toArray()[0], com);
    }
}
