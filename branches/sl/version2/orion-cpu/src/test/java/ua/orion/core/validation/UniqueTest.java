package ua.orion.core.validation;

import ua.orion.core.validation.UniqueConstraintValidator;
import java.util.Set;
import javax.persistence.*;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.tapestry5.ioc.internal.services.PropertyAccessImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.test.IOCTestCase;
import org.hibernate.ejb.Ejb3Configuration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.jpa.JPAEntityManagerSource;
import org.tynamo.jpa.JPATransactionManager;
import org.tynamo.jpa.internal.JPATransactionManagerImpl;
import test.foo.entities.City;
import test.foo.entities.Document;
import test.foo.entities.Person;
import test.foo.entities.State;

/**
 *
 * @author sl
 */
//TODO Дописать тесты
public class UniqueTest extends IOCTestCase {

    EntityManagerFactory emf;
    JPATransactionManager txManager;
    JPAEntityManagerSource emSource;
    UniqueConstraintValidator uValidator;
    static PropertyAccess propertyAccess = new PropertyAccessImpl();

    @BeforeClass(alwaysRun=true)
    public void setUp() throws Exception {
        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.addAnnotatedClass(City.class);
        cfg.addAnnotatedClass(Document.class);
        cfg.addAnnotatedClass(Person.class);
        cfg.addAnnotatedClass(State.class);
        cfg.configure("hibernate.cfg.xml");
        emf = cfg.buildEntityManagerFactory();
    }

    @AfterClass
    public void tearDown() throws Exception {
       // verify();
        emf.close();
    }

    @BeforeMethod
    public void prepare() {
        emSource = this.newMock(JPAEntityManagerSource.class);
        expect(emSource.create()).andReturn(emf.createEntityManager()).anyTimes();
        expect(emSource.getEntityManagerFactory()).andReturn(emf).anyTimes();
        replay();
        txManager = new JPATransactionManagerImpl(emSource);
        uValidator = new UniqueConstraintValidator();
        UniqueConstraintValidator.setENTITY_MANAGER_FACTORY(emSource.getEntityManagerFactory());
        UniqueConstraintValidator.setPROPERTY_ACCESS(propertyAccess);
    }
    
    @AfterMethod
    public void unPrepare() {
        txManager.abort();
    }

    @Test(expectedExceptions=javax.validation.ConstraintViolationException.class)
    public void column() {
        Document doc = new Document();
        doc.setSerial("AS");
        doc.setNumber("123456");
        txManager.getEntityManager().persist(doc);
        Document doc1 = new Document();
        doc1.setSerial("AS");
        doc1.setNumber("123456");
        txManager.getEntityManager().persist(doc1);
    }

    @Test(expectedExceptions=javax.validation.ConstraintViolationException.class)
    public void attributeOverride() {
        State state1 = new State();
        state1.setName("USA");
        txManager.getEntityManager().persist(state1);
        State state2 = new State();
        state2.setName("USA");
        txManager.getEntityManager().persist(state2);
    }

    //@Test(expectedExceptions=javax.validation.ConstraintViolationException.class)
    public void attributeOverrides() {
    }
    
    //@Test(expectedExceptions=javax.validation.ConstraintViolationException.class)
    public void joinColumn() {
    }
    
    //@Test(expectedExceptions=javax.validation.ConstraintViolationException.class)
    public void assotiationOverride() {
    }
    
    //@Test(expectedExceptions=javax.validation.ConstraintViolationException.class)
    public void assotiationOverrides() {
    }
    //TODO Message Test
}
