
import javax.persistence.*;
import org.hibernate.ejb.Ejb3Configuration;
import org.testng.annotations.Test;
import test.foo.entities.State;

/**
 *
 * @author user
 */
public class NewClass {

    @Test
    public void setUp() throws Exception {
        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.addAnnotatedClass(State.class);
        cfg.configure("hibernate.cfg.xml");
        EntityManagerFactory emf = cfg.buildEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        State state1 = new State();
        state1.setName("USA");
        em.persist(state1);
        State state2 = new State();
        state2.setName("USA");
        em.persist(state2);
    }
}
