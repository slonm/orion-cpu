package test.testset;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Тестируем подключение в базе данных
 * @author dobro
 */
public class T1 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        emf.close();
    }
}
