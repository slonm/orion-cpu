package ua.orion.cpu.licensing.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import org.apache.tapestry5.ioc.internal.services.ClassNameLocatorImpl;
import org.apache.tapestry5.ioc.internal.services.ClasspathURLConverterImpl;
import org.hibernate.ejb.Ejb3Configuration;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.tynamo.jpa.JPAEntityPackageManager;
import org.tynamo.jpa.internal.DatabaseSchemaObjectCreator;
import ua.orion.cpu.entities.Document;
import ua.orion.cpu.orgunits.entities.Chair;

/**
 *
 * @author user
 */
public class LicenseRecordTest{
    
    @Test
    public void setUp() throws Exception {
        Ejb3Configuration cfg = new Ejb3Configuration();
        cfg.addAnnotatedClass(Document.class);
        cfg.addAnnotatedClass(License.class);
        cfg.addAnnotatedClass(LicenseRecord.class);
        cfg.addAnnotatedClass(EducationForm.class);
        cfg.addAnnotatedClass(EducationalQualificationLevel.class);
        cfg.addAnnotatedClass(LicenseRecordGroup.class);
        cfg.addAnnotatedClass(KnowledgeAreaOrTrainingDirection.class);
        cfg.addAnnotatedClass(TrainingDirectionOrSpeciality.class);
        cfg.addAnnotatedClass(Chair.class);
        DatabaseSchemaObjectCreator schemaCreator=new DatabaseSchemaObjectCreator(LoggerFactory.getLogger("DatabaseSchemaObjectCreator"), new JPAEntityPackageManager() {

            @Override
            public Collection<String> getPackageNames() {
                return Arrays.asList("ua.orion.cpu.licensing.entities", "ua.orion.cpu.orgunits.entities");
            }
        },new ClassNameLocatorImpl(new ClasspathURLConverterImpl()),"CREATE SCHEMA %s AUTHORIZATION SA");
        schemaCreator.configure(cfg);
        cfg.configure("hibernate.cfg.xml");
        EntityManagerFactory emf = cfg.buildEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        KnowledgeAreaOrTrainingDirection kaotd = new KnowledgeAreaOrTrainingDirection();
        kaotd.setCode("1234");
        kaotd.setName("kaotd1");
        em.persist(kaotd);
        TrainingDirectionOrSpeciality tdos=new TrainingDirectionOrSpeciality();
        tdos.setCode("1234");
        tdos.setIsTrainingDirection(Boolean.TRUE);
        tdos.setKnowledgeAreaOrTrainingDirection(kaotd);
        tdos.setName("tdos1");
        em.persist(tdos);
        EducationForm efStat=new EducationForm();
        efStat.setName("Stat");
        em.persist(efStat);
        EducationForm efCorr=new EducationForm();
        efCorr.setName("Corr");
        em.persist(efCorr);
        EducationalQualificationLevel eql=new EducationalQualificationLevel();
        eql.setCode("5");
        eql.setName("first");
        em.persist(eql);
        LicenseRecordGroup lrg=new LicenseRecordGroup();
        lrg.setName("lrg");
        em.persist(lrg);
        License l=new License();
        l.setNumber("123456");
        l.setSerial("АА");
        l.setIssue(new Date());
        em.persist(l);
        Chair ch=new Chair();
        ch.setName("chair");
        ch.setShortName("chair");
        em.persist(ch);
        LicenseRecord lr=new LicenseRecord();
        lr.setEducationalQualificationLevel(eql);
        lr.setLicense(l);
        lr.setLicenseRecordGroup(lrg);
        lr.setTrainingDirectionOrSpeciality(tdos);
        lr.setOrgUnit(ch);
        lr.setTerminationDate(new Date());
        lr.getLicenseQuantityByEducationForm().put(efStat, 1);
        lr.getLicenseQuantityByEducationForm().put(efCorr, 2);
        em.persist(lr);
        em.getTransaction().commit();
    }
}
