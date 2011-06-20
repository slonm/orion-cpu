package ua.orion.cpu.core.persons.services;

import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.LicensingSymbols;
import ua.orion.cpu.core.persons.PersonsSymbols;
import ua.orion.cpu.core.persons.entities.AcademicRank;
import ua.orion.cpu.core.persons.entities.Person;
import ua.orion.cpu.core.persons.entities.ScienceArea;
import ua.orion.cpu.core.persons.entities.ScientificDegree;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;

/**
 *
 * @author kgp
 */
//@OrderLibrary("after:" + LicensingSymbols.LICENSING_LIB)
public class PersonsSeedEntity {
    public PersonsSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(PersonsSymbols.PERSONS_LIB));
        if (testData) {

            //---Списки доступа----------
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "Person:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender", SubjectType.ROLE, "Person:read,insert,update,menu"));

            //---Области наук
            ScienceArea sa_PhysMath = es.findUniqueOrPersist(new ScienceArea("фізико-математичних наук", "ф.-м.н."));
            ScienceArea sa_Econom = es.findUniqueOrPersist(new ScienceArea("економіччних наук", "е.н."));
            //---Учёные степени
            ScientificDegree sd_Cand = es.findUniqueOrPersist(new ScientificDegree("кандидат", "к."));
            ScientificDegree sd_Doc = es.findUniqueOrPersist(new ScientificDegree("доктор", "д."));
            //---Учёные звания
            AcademicRank ar_ProfAssist = es.findUniqueOrPersist(new AcademicRank("доцент", "доц."));
            AcademicRank ar_Prof = es.findUniqueOrPersist(new AcademicRank("професор", "проф."));
            //---Персоны
            Person p_TestPerson1 = es.findUniqueOrPersist(new Person("Іван", "Іванович", "Іванов", sd_Cand, sa_PhysMath, ar_ProfAssist));
        }
    }
}
