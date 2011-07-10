package test.testset;

import java.beans.IntrospectionException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import org.apache.tapestry5.ioc.internal.services.PropertyAccessImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.impl.*;
import orion.tapestry.grid.lib.jpa.GridModelJPABean;
import orion.tapestry.grid.lib.model.GridModelInterface;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorException;
import orion.tapestry.grid.services.GridFieldFactory;
import orion.tapestry.grid.services.GridFieldFactoryImpl;
import test.entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.tapestry.grid.lib.rows.GridRow;

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
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
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
        Predicate p = cb.equal(from.get("name"), "student1");
        query.where(p);

        // выполняем запрос
        TypedQuery q = em.createQuery(query);
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
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
        Predicate p1 = cb.lessThanOrEqualTo(from.get("name"), "student7");
        Predicate p2 = cb.greaterThanOrEqualTo(from.get("name"), "student2");
        Predicate p3 = cb.and(p1, p2);
        query.where(p3);

        // выполняем запрос
        TypedQuery q = em.createQuery(query);
        List all = q.getResultList();

        // печатаем результаты
        for (Object row : all) {
            Student st = (Student) row;
            System.out.println(st.getId() + "  " + st.getName());
        }
    }

    public static GridFieldFactory getGridFieldFactory() {

        Map<String, Class> configuration = new TreeMap<String, Class>();
        configuration.put("java.lang.String", GridFieldString.class);

        configuration.put("byte", GridFieldNumberByte.class);
        configuration.put("java.lang.Byte", GridFieldNumberByte.class);

        configuration.put("short", GridFieldNumberShort.class);
        configuration.put("java.lang.Short", GridFieldNumberShort.class);

        configuration.put("int", GridFieldNumberInteger.class);
        configuration.put("java.lang.Integer", GridFieldNumberInteger.class);

        configuration.put("long", GridFieldNumberLong.class);
        configuration.put("java.lang.Long", GridFieldNumberLong.class);

        configuration.put("float", GridFieldNumberFloat.class);
        configuration.put("java.lang.Float", GridFieldNumberFloat.class);

        configuration.put("double", GridFieldNumberDouble.class);
        configuration.put("java.lang.Double", GridFieldNumberDouble.class);

        configuration.put("java.util.Date", GridFieldDate.class);

        configuration.put("boolean", GridFieldBoolean.class);
        configuration.put("java.lang.Boolean", GridFieldBoolean.class);

        Logger logger = LoggerFactory.getLogger(T6.class);

        PropertyAccess propertyAccess = new PropertyAccessImpl();
        return new GridFieldFactoryImpl(configuration, logger, propertyAccess);
    }

    public static void main(String[] args) throws RestrictionEditorException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
        EntityManager em = emf.createEntityManager();
        T6 cls = new T6(em);
        cls.fillDb();

        GridFieldFactory gridFieldFactory = getGridFieldFactory();

        try {
            GridModelInterface model = new GridModelJPABean(Student.class, gridFieldFactory, em);
            List<GridFieldAbstract> fields = model.getFields();
            for (GridFieldAbstract fld : fields) {
                System.out.println(fld.getAttributeName() + "  " + fld.getUid());
            }

            model.setFieldSortList(model.getFieldSortList());

            List<GridRow> rows= model.getRows();
            System.out.println("_________________________________");
            for(GridRow row:rows){
                System.out.println(row.getValue("id")+"  "+row.getValue("name"));
            }
            System.out.println("_________________________________");
        } catch (IntrospectionException ex) {
            //Logger.getLogger(T6.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.close();
        emf.close();
    }
}
