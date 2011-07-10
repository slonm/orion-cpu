package test.testset;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Тестируем подключение в базе данных
 * @author dobro
 */
public class T2 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }
}
