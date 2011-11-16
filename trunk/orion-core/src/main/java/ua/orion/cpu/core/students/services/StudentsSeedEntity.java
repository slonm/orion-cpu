package ua.orion.cpu.core.students.services;

import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.EducationForm;
import ua.orion.cpu.core.licensing.entities.KnowledgeAreaOrTrainingDirection;
import ua.orion.cpu.core.licensing.entities.TrainingDirectionOrSpeciality;
import ua.orion.cpu.core.persons.entities.Person;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;
import ua.orion.cpu.core.students.StudentsSymbols;
import ua.orion.cpu.core.students.entities.AcademicGroup;
import ua.orion.cpu.core.students.entities.AcademicStream;
import ua.orion.cpu.core.students.entities.Student;

/**
 *
 * @author kgp
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
            //Тестовая персона
            Person person = es.findUniqueOrPersist(new Person("Петро", "Олександрович", "Коваленко", null, null, null));
            //Создание учебного потока. Подтягивание других сущностей. 
            KnowledgeAreaOrTrainingDirection knowledgeAreaOrTrainingDirection = es.findUniqueOrPersist(new KnowledgeAreaOrTrainingDirection("Специфічні категорії", null, "0000", false, false));
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality = es.findUniqueOrPersist(new TrainingDirectionOrSpeciality("Педагогіка вищої школи", "ПВШ", "05", false, knowledgeAreaOrTrainingDirection, false));
            EducationForm educationForm = es.findUniqueOrPersist(new EducationForm("Денна", "Ден.", EducationForm.STATIONARY_UKEY, 1));
            AcademicStream academicStream = es.findUniqueOrPersist(new AcademicStream(trainingDirectionOrSpeciality, educationForm, Integer.valueOf(3)));
            //Создание академ. группы
            AcademicGroup academicGroup = es.findUniqueOrPersist(new AcademicGroup("ПВ-107", academicStream));
            //Создание записи студента для персоны с указанием учебного потока и академ группы
            Student student = es.findUniqueOrPersist(new Student(person, academicStream, academicGroup));
        }
    }
}
