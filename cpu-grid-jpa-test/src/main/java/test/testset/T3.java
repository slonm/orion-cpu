package test.testset;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import test.entities.Student;

/**
 * Тестируем подключение в базе данных
 * @author dobro
 */
public class T3 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(new Student());
        em.flush();
        em.persist(new Student());
        em.flush();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
