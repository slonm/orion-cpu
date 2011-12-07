/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.employees.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.DateTimeUtils;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.employees.EmployeeSymbols;
import ua.orion.cpu.core.employees.entities.Employee;
import ua.orion.cpu.core.employees.entities.EmployeePost;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.cpu.core.orgunits.entities.OrgUnitPost;
import ua.orion.cpu.core.orgunits.entities.Post;
import ua.orion.cpu.core.orgunits.entities.University;
import ua.orion.cpu.core.persons.entities.*;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;

/**
 *
 * @author molodec
 */
@OrderLibrary("after:" + EmployeeSymbols.EMPLOYEE_LIB)
public class EmployeesSeedEntity {

    public EmployeesSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        if (testData) {
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "Employee:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender", SubjectType.ROLE, "Employee:read,insert,update,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "EmployeePost:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender", SubjectType.ROLE, "EmployeePost:read,insert,update,menu"));
            NatureResidence nr_Oc = es.findUniqueOrPersist(new NatureResidence("Обласний центр", "о.ц."));
            Country country_ukraine =
                    es.findUniqueOrPersist(new Country("Україна", "Українська",
                    "Київ", "Президентсько-парламентська республіка", "Гривня",
                    "До складу України входять 27 адміністративних одиниць"));
            //---Регионы
            Region reg_donbas = es.findUniqueOrPersist(new Region("Донбас", country_ukraine, null));
            //---Область
            District dist_lugansk = es.findUniqueOrPersist(new District("Луганська область", reg_donbas, null));
            //---Місто
            City city_lugansk = es.findUniqueOrPersist(new City("Луганськ", country_ukraine, reg_donbas, dist_lugansk, nr_Oc, null, null));
            //---Гражданство
            Citizenship czship_ukr = es.findUniqueOrPersist(new Citizenship("Українське", country_ukraine));
            //---Семейное положение 
            FamilyStatus familyStatus = es.findUniqueOrPersist(new FamilyStatus("Одружений"));
            //---Родство
            Cognation cognation = es.findUniqueOrPersist(new Cognation("Дружина", Boolean.FALSE));
            //---Области наук
            ScienceArea sa_PhysMath = es.findUniqueOrPersist(new ScienceArea("фізико-математичних наук", "ф.-м.н."));
            ScienceArea sa_Econom = es.findUniqueOrPersist(new ScienceArea("економіччних наук", "е.н."));
            //---Учёные степени
            ScientificDegree sd_Cand = es.findUniqueOrPersist(new ScientificDegree("кандидат", "к."));
            ScientificDegree sd_Doc = es.findUniqueOrPersist(new ScientificDegree("доктор", "д."));
            //---Учёные звания
            AcademicRank ar_ProfAssist = es.findUniqueOrPersist(new AcademicRank("доцент", "доц."));
            AcademicRank ar_Prof = es.findUniqueOrPersist(new AcademicRank("професор", "проф."));
            //---Пол
            Sex male = es.findUniqueOrPersist(new Sex("Чоловічий", "ч."));
            //---Типы трудовых договоров
            es.findUniqueOrPersist(new TypeAgreement("Трудова угода", "труд.угода"));
            es.findUniqueOrPersist(new TypeAgreement("Сумісник за контрактом", "сумісн.за контракт."));
            //---Типы трудовых отношений
            es.findUniqueOrPersist(new LabourRelations("Штатний працівник"));
            es.findUniqueOrPersist(new LabourRelations("Сумісник"));
            //---Пасспорт
            Passport pass_person = es.findUniqueOrPersist(new Passport("СК", "543543545", DateTimeUtils.createCalendar(16, 3, 2001), "Нов.-Волинським МРВ УМВС України в Житомирській обл."));
            SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(EmployeeSymbols.EMPLOYEE_LIB));
            University ou_CPU = es.findUniqueOrPersist(new University("Класичний приватний університет", "КПУ"));
            Post post = es.findUniqueOrPersist(new Post("Методист інституту управління", "МІУ"));
            OrgUnitPost orgUnitPost = es.findUniqueOrPersist(new OrgUnitPost(ou_CPU, post, Double.valueOf(20.0)));
            EmployeePost employeePost = es.findUniqueOrPersist(new EmployeePost(orgUnitPost, Boolean.TRUE));
            Set<EmployeePost> employeePosts = new HashSet();
            employeePosts.add(employeePost);
            Employee employee = es.findUniqueOrPersist(new Employee(sd_Doc, sa_Econom, ar_Prof, "Олександр", "Вікторович", "Богданов", DateTimeUtils.createCalendar(12, 11, 1984), male, czship_ukr, "0987654321", pass_person, DateTimeUtils.createCalendar(5, 3, 2010), ""));
            employee.setFamilyStatus(familyStatus);
            es.findUniqueOrPersist(employee);
            StructureFamily sf_zhn = es.findUniqueOrPersist(new StructureFamily(employee, "Оксана", "Богданова", "Вікторівна", cognation, DateTimeUtils.createCalendar(2, 10, 1983), "КПУ", "ст. Викладач", "234-24-21"));
        }
    }
}
