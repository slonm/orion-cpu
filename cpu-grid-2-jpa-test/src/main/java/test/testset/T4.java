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
import test.tapestry.entities.Student;

/**
 * Тестируем подключение в базе данных
 * @author dobro
 */
public class T4 {

    private EntityManager em;

    public T4(EntityManager _em) {
        this.em = _em;
    }

    // заповнюємо базу даних
    public void fillDb() {
        Student st;

        this.em.getTransaction().begin();
        this.em.createQuery("DELETE FROM Student").executeUpdate();
        for (long id = 0; id < 10; id++) {
            st = new Student();
            st.setId(id);
            st.setName("student" + id);
            st.setDateOfBirth(null);
            em.persist(st);
            em.flush();
        }
        this.em.getTransaction().commit();
    }

    // show meta information
    public void showMeta() {
        Metamodel m = em.getMetamodel();
        EntityType<Student> et = m.entity(Student.class);

        System.out.println("type = " + et.getIdType().getJavaType().getName());

        System.out.println("id   = " + et.getId(et.getIdType().getJavaType()).getName());
    }

    public void selectAll() {
        // редактор запроса
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // запрос
        CriteriaQuery query = cb.createQuery(Student.class);

        // описание сущности
        Root from = query.from(Student.class);

        // запрос типа select
        query.select(from);

        // выполняем запрос
        TypedQuery q = em.createQuery(query);
        List all = q.getResultList();

        // печатаем результаты
        for(Object row:all){
            Student st=(Student)row;
            System.out.println(st.getId()+"  "+st.getName());
        }
    }

    public void select1() {
        // редактор запроса
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // запрос
        CriteriaQuery query = cb.createQuery(Student.class);

        // описание сущности
        Root from = query.from(Student.class);

        // запрос типа select
        query.select(from);

        // добавляем условие выборки
        //query.where(cb.equal(from.get("name")), "Fido");
        Predicate p=cb.equal(from.get("name"), "student1");
        query.where(p);

        // выполняем запрос
        TypedQuery q = em.createQuery(query);
        List all = q.getResultList();

        // печатаем результаты
        for(Object row:all){
            Student st=(Student)row;
            System.out.println(st.getId()+"  "+st.getName());
        }
    }



    public void select2() {
        // редактор запроса
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // запрос
        CriteriaQuery query = cb.createQuery(Student.class);

        // описание сущности
        Root from = query.from(Student.class);

        // запрос типа select
        query.select(from);

        // добавляем условие выборки
        Predicate p1=cb.lessThanOrEqualTo(from.get("name"), "student7");
        Predicate p2=cb.greaterThanOrEqualTo(from.get("name"), "student2");
        Predicate p3=cb.and(p1,p2);
        query.where(p3);

        // выполняем запрос
        TypedQuery q = em.createQuery(query);
        List all = q.getResultList();

        // печатаем результаты
        for(Object row:all){
            Student st=(Student)row;
            System.out.println(st.getId()+"  "+st.getName());
        }
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        EntityManager em = emf.createEntityManager();
        T4 cls = new T4(em);
        cls.fillDb();
        cls.showMeta();
        // cls.selectAll();
        //cls.select1();
        cls.select2();
        em.close();
        emf.close();
    }
}
