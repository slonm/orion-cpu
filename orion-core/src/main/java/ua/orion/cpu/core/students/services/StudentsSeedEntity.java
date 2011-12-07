package ua.orion.cpu.core.students.services;

import java.util.HashSet;
import java.util.Set;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.DateTimeUtils;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.cpu.core.persons.entities.*;
import ua.orion.cpu.core.security.entities.*;
import ua.orion.cpu.core.students.StudentsSymbols;
import ua.orion.cpu.core.students.entities.*;

/**
 *
 * @author molodec
 */
@OrderLibrary("after:" + StudentsSymbols.STUDENTS_LIB)
public class StudentsSeedEntity {

    public StudentsSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(StudentsSymbols.STUDENTS_LIB));
        if (testData) {
            //Права доступа
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader", SubjectType.ROLE, "Student:read,menu"));
            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender", SubjectType.ROLE, "Student:read,insert,update,menu"));
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
            //---Пол
            Sex male = es.findUniqueOrPersist(new Sex("Чоловічий", "ч."));
            Sex female = es.findUniqueOrPersist(new Sex("Жіночий", "ж."));
            //---Пасспорт
            Passport pass_person = es.findUniqueOrPersist(new Passport("СК", "8768768678", DateTimeUtils.createCalendar(11, 3, 1999), "Нов.-Волинським МРВ УМВС України в Житомирській обл."));
            //Создание учебного потока. Подтягивание других сущностей. 
            KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Специфічні категорії", null, "0000", false, false));
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Педагогіка вищої школи", "ПВШ", "05", false, knowledgeAreaOrTrainingDirection, false));
            EducationForm educationForm = es.findUniqueOrPersist(new EducationForm("Денна", "Ден.", EducationForm.STATIONARY_UKEY, 1));
            AcademicStream academicStream = es.findUniqueOrPersist(new AcademicStream(trainingDirectionOrSpeciality, educationForm, Integer.valueOf(3)));
            AcademicGroup academicGroup = es.findUniqueOrPersist(new AcademicGroup("ПВ-107", academicStream));
            Education education = es.findUniqueOrPersist(new Education("Середня технічна"));
            es.findUniqueOrPersist(new Education("Вища"));
            es.findUniqueOrPersist(new Education("Повна загальна середня"));
            es.findUniqueOrPersist(new Education("Базова загальна середня"));
            KindPayer kindPayer = es.findUniqueOrPersist(new KindPayer("Фізична особа", "Фіз.особа"));
            ContractStudent contractStudent = es.findUniqueOrPersist(new ContractStudent("3224141", DateTimeUtils.createCalendar(4, 5, 2005), kindPayer, "м. Львів. Ул. Правди 40"));
            Set<EducationStudent> educationStudent = new HashSet<EducationStudent>();
            educationStudent.add(es.findUniqueOrPersist(new EducationStudent(null, educationForm, DateTimeUtils.createCalendar(4, 5, 2005), contractStudent)));
            Student student = new Student(academicStream, academicGroup, education, "Ольга", "Олександрівна", "Іванова", DateTimeUtils.createCalendar(4, 5, 1983), male, czship_ukr, "234124125412", pass_person, DateTimeUtils.createCalendar(4, 3, 2009), "");
            student.setEducationStudent(educationStudent);
            es.findUniqueOrPersist(student);

        }
    }
}
