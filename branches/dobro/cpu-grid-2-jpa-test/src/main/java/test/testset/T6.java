package test.testset;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorJPACriteria;
import test.entities.Book;
import test.entities.Publisher;
import test.entities.Student;

/**
 * Тестируем подключение в базе данных
 * @author dobro
 */
public class T6 {

    private EntityManager em;

    public T6(EntityManager _em) {
        this.em = _em;
    }

    // заповнюємо базу даних
    public void fillDb() {
        Student st;

        this.em.getTransaction().begin();
        this.em.createQuery("DELETE FROM Book").executeUpdate();
        this.em.createQuery("DELETE FROM Publisher").executeUpdate();

        // ------------- fill-in the database tables - begin -------------------
        // add Publisher 1
        Publisher pu1 = new Publisher("izdatel");
        em.persist(pu1);

        // add Publisher 2
        Publisher pu2 = new Publisher("vydavnyk");
        em.persist(pu2);

        // add Book1
        Book b1 = new Book("Skazky", pu1);
        em.persist(b1);

        Book b3 = new Book("Skazky.ru", pu1);
        em.persist(b3);

        Book b2 = new Book("Kazky", pu2);
        em.persist(b2);

        Book b4 = new Book("Kazky.ua", pu2);
        em.persist(b4);
        this.em.getTransaction().commit();
    }

    public static void testEq0(EntityManager em) throws RestrictionEditorException {
        System.out.println("testEq0 ===================== ");

        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Book.class, em);

        edit.createEmpty();

        // тест 1
        edit.constField("title");
        edit.constValue("Kazky");
        edit.eq();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Book st = (Book) row;
            System.out.println(st.getId() + "\t" + st.getTitle()+"\t"+st.getPublisher().getTitle());
        }

    }

    public static void testEq1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testEq1 ===================== ");

        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Book.class, em);

        edit.createEmpty();

        // тест 1
        edit.constField("publisher.title");
        edit.constValue("vydavnyk");
        edit.eq();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Book st = (Book) row;
            System.out.println(st.getId() + "\t" + st.getTitle()+"\t"+st.getPublisher().getTitle());
        }

    }

    public static void testEq2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testEq2 ===================== ");

        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Book.class, em);

        edit.createEmpty();

        // тест 1
        edit.constValue("vydavnyk");
        edit.constField("publisher.title");
        edit.eq();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Book st = (Book) row;
            System.out.println(st.getId() + "\t" + st.getTitle()+"\t"+st.getPublisher().getTitle());
        }
    }

    public static void testGt1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testGt1 ===================== ");

        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Book.class, em);

        edit.createEmpty();

        // тест 1
        edit.constField("publisher.title");
        edit.constValue("izdatel");
        edit.gt();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Book st = (Book) row;
            System.out.println(st.getId() + "\t" + st.getTitle()+"\t"+st.getPublisher().getTitle());
        }
    }

    public static void testGt2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testGt2 ===================== ");

        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Book.class, em);

        edit.createEmpty();

        // тест 1
        edit.constValue("vydavnyk");
        edit.constField("publisher.title");
        edit.gt();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Book st = (Book) row;
            System.out.println(st.getId() + "\t" + st.getTitle()+"\t"+st.getPublisher().getTitle());
        }
    }

    public static void testGe1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testGe1 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constValue("student5");
        edit.constField("name");
        edit.ge();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testGe2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testGe2 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constField("name");
        edit.constValue("student5");
        edit.ge();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testLt1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testLt1 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constField("name");
        edit.constValue("student5");
        edit.lt();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testLt2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testLt2 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constValue("student5");
        edit.constField("name");
        edit.lt();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testLe1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testLe1 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constField("name");
        edit.constValue("student5");
        edit.le();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testLe2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testLe2 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constValue("student5");
        edit.constField("name");
        edit.le();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testNeq(EntityManager em) throws RestrictionEditorException {
        System.out.println("testNeq ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест 1
        edit.constValue("student5");
        edit.constField("name");
        edit.neq();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testLike1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testLike1 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.constValue("%5");
        edit.like();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testLike2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testLike2 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constValue("student5");
        edit.constField("name");
        edit.like();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testContains1(EntityManager em) throws RestrictionEditorException {
        System.out.println("testContains1 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.constValue("5");
        edit.contains();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testContains2(EntityManager em) throws RestrictionEditorException {
        System.out.println("testContains2 ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constValue("student51111111");
        edit.constField("name");
        edit.contains();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testIsNull(EntityManager em) throws RestrictionEditorException {
        System.out.println("testIsNull ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.isNull();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testIsNotNull(EntityManager em) throws RestrictionEditorException {
        System.out.println("testIsNotNull ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.isNotNull();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testIn(EntityManager em) throws RestrictionEditorException {
        System.out.println("testIsNull ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.constValueList("student1", "student2", "student3");
        edit.in();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testNot(EntityManager em) throws RestrictionEditorException {
        System.out.println("testIsNull ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.isNull();
        edit.not();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testAnd(EntityManager em) throws RestrictionEditorException {
        System.out.println("testAnd ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.constValue("student5");
        edit.gt();

        edit.constField("name");
        edit.constValue("student8");
        edit.lt();

        edit.and();
        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void testOr(EntityManager em) throws RestrictionEditorException {
        System.out.println("testOr ===================== ");
        RestrictionEditorJPACriteria edit = new RestrictionEditorJPACriteria(Student.class, em);

        edit.createEmpty();

        // тест
        edit.constField("name");
        edit.constValue("student5");
        edit.lt();

        edit.constField("name");
        edit.constValue("student8");
        edit.gt();

        edit.or();

        // выполняем запрос
        TypedQuery q = em.createQuery(edit.getValue());
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static void main(String[] args) throws RestrictionEditorException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        EntityManager em = emf.createEntityManager();
        T6 cls = new T6(em);
        cls.fillDb();

        testEq0(em);
        testEq1(em);
        testEq2(em);
        testGt1(em);
        testGt2(em);
        //testGe1(em);
        //testGe2(em);
        //testLt1(em);
        //testLt2(em);
        //testLe1(em);
        //testLe2(em);
        //testNeq(em);
        //testLike1(em);
        //testLike2(em);
        //testContains1(em);
        //testContains2(em);
        //testIsNull(em);
        //testIsNotNull(em);
        //testIn(em);
        //testNot(em);
        //testAnd(em);
        // testOr(em);


        em.close();
        emf.close();
    }
}
