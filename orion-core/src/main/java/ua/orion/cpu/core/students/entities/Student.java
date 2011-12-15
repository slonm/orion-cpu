package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ua.orion.cpu.core.persons.entities.Citizenship;
import ua.orion.cpu.core.persons.entities.Passport;
import ua.orion.cpu.core.persons.entities.Person;
import ua.orion.cpu.core.persons.entities.Sex;

/**
 * Студенты.
 *
 * @author sl
 */
@Entity
@Table(schema = "stu")
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends Person {

    private static final long serialVersionUID = 1L;
//    /**
//     * Академ. поток
//     */
//    @NotNull
//    private AcademicStream academicStream;
    /**
     * Академ. группа
     */
//    @NotNull
//    private AcademicGroup academicGroup;
    /**
     * Образование студента. Например: Высшее образование, Среднее образование и
     * другие
     */
    private Education education;
    /**
     * Записи о том, на каких специальностях на каких формах обучения учится
     * студент. Допустим, если на одной специальности он учится на дневной, на
     * другой - на заочной форме обучения, к нему будут привязаны 2 записи.
     */
    private Set<EducationStudent> educationStudent = new HashSet();

    /**
     * Конструктор по умолчанию для создания студента. Для сохранения нужно об-
     * язательно установить значения полей academicStream,academicGroup и тех
     * полей, которые обязательны для персоны.
     */
    public Student() {
    }

    /**
     * Создание студента на основе:
     *
     * @param academicStream - академ. поток
     * @param academicGroup - академ. группа
     * @param education - образование
     * @param firstName - имя
     * @param patronymicName - отчество
     * @param surname - фамилия
     * @param birthday - дата рождения
     * @param sex - пол человека
     * @param citizenship - гражданство
     * @param handle - идентификационный код
     * @param passport - данные паспорта
     * @param datePhliugraphia - дата прохождения флюрографии
     * @param notes - заметки
     */
    public Student(/*AcademicStream academicStream, AcademicGroup academicGroup,*/
            Education education, String firstName, String patronymicName,
            String surname, Calendar birthday, Sex sex,
            Citizenship citizenship, String handle, Passport passport,
            Calendar datePhliugraphia, String notes) {
        super(firstName, patronymicName, surname, birthday, sex, citizenship,
                handle, passport, datePhliugraphia, notes);
//        this.academicStream = academicStream;
//        this.academicGroup = academicGroup;
        this.education = education;
    }

    /**
     * Получение академ. группы
     *
     * @return академ группа
     */
//    @ManyToOne
//    public AcademicGroup getAcademicGroup() {
//        return academicGroup;
//    }

    /**
     * Установка академ группы
     *
     * @param academicGroup - академ. группа.
     */
//    public void setAcademicGroup(AcademicGroup academicGroup) {
//        this.academicGroup = academicGroup;
//    }

    /**
     * Получение академ потока
     *
     * @return академ. поток
     */
//    @ManyToOne
//    public AcademicStream getAcademicStream() {
//        return academicStream;
//    }

    /**
     * Установка академ. потока
     *
     * @param academicStream академ поток.
     */
//    public void setAcademicStream(AcademicStream academicStream) {
//        this.academicStream = academicStream;
//    }

    /**
     * Получения образования студента
     *
     * @return образование(справочник)
     */
    @ManyToOne
    public Education getEducation() {
        return education;
    }

    /**
     * Установка образования сутдента
     *
     * @param education образование студента
     */
    public void setEducation(Education education) {
        this.education = education;
    }

    /**
     * Получения набора направлений, специальностей на которых учится студент с
     * формами обучения и прочей информацией.
     *
     * @return
     */
    @OneToMany
    public Set<EducationStudent> getEducationStudent() {
        return educationStudent;
    }

    /**
     * Установка специальностей на которых учится студент.
     *
     * @param educationStudent
     */
    public void setEducationStudent(Set<EducationStudent> educationStudent) {
        this.educationStudent = educationStudent;
    }

    /**
     * Добавить одну специальность к уже имеющимся на которы учится студент
     */
    public void addEducationStudent(EducationStudent educationStudent) {
        this.educationStudent.add(educationStudent);
    }
}
