package ua.orion.core.validation;

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

/**
 *
 * @author sl
 */
public class UniqueConstraintValidatorTest extends IOCTestCase {

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
        uValidator.setEntityManager(txManager.getEntityManager());
//        UniqueConstraintValidator.setENTITY_MANAGER_FACTORY(emSource.getEntityManagerFactory());
        UniqueConstraintValidator.setPROPERTY_ACCESS(propertyAccess);
    }
    
    @AfterMethod
    public void unPrepare() {
        txManager.abort();
    }

    @Test
    public void getPersistentUniqueObject_SingilarAttribute() {
        City city = new City();
        city.setName("Kiev");
        txManager.getEntityManager().persist(city);
        City city1 = new City();
        city1.setName("Kiev");
        Object result = uValidator.getPersistentUniqueObject(city1);
        assertEquals("Kiev", result.toString());
    }

    @Test
    public void getPersistentUniqueObject_UniqueConstraint() {
        Document doc = new Document();
        doc.setSerial("QW");
        doc.setNumber("123456");
        txManager.getEntityManager().persist(doc);
        Document doc1 = new Document();
        doc1.setSerial("QW");
        doc1.setNumber("123456");
        Object result = uValidator.getPersistentUniqueObject(doc1);
        assertEquals("QW123456", result.toString());
    }
    
    @Test
    public void uniqueAnnotationFactory() {
        Document doc = new Document();
        doc.setSerial("ZX");
        doc.setNumber("123456");
        txManager.getEntityManager().persist(doc);
        txManager.commit();
        Document doc1 = new Document();
        doc1.setSerial("ZX");
        doc1.setNumber("123456");
        Configuration<?> config = Validation.byDefaultProvider().configure();
        UniqueConstraintValidator.setPROPERTY_ACCESS(propertyAccess);
        config.constraintValidatorFactory(new UniqueConstraintValidatorFactory(txManager.getEntityManager()));
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Document>> violations = validator.validate(doc1);
        assertEquals(violations.size(), 1);
        ConstraintViolation<Document> violation=violations.toArray(new ConstraintViolation[1])[0];
        assertEquals(violation.getMessageTemplate(), UniqueConstraintValidator.MESSAGE);
    }
    
    @Test
    public void unmanagedEntity() {
        City city = new City();
        city.setName("Zap");
        txManager.getEntityManager().persist(city);
        Document doc = new Document();
        doc.setSerial("we");
        doc.setNumber("123456a");
        txManager.getEntityManager().persist(doc);
        Person person=new Person();
        person.setFio("SMA");
        person.setAddressCity(city);
        person.getDocuments().add(doc);
        
        Configuration<?> config = Validation.byDefaultProvider().configure();
        UniqueConstraintValidator.setPROPERTY_ACCESS(propertyAccess);
        config.constraintValidatorFactory(new UniqueConstraintValidatorFactory(txManager.getEntityManager()));
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertEquals(violations.size(), 0);
    }
}
