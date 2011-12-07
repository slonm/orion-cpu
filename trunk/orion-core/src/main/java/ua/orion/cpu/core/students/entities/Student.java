package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import ua.orion.cpu.core.persons.entities.*;
import javax.persistence.*;
import javax.validation.constraints.*;

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
    @NotNull
    private AcademicStream academicStream;
    @NotNull
    private AcademicGroup academicGroup;
        private Education education;
    private Set<EducationStudent> educationStudent = new HashSet();

    public Student() {
    }

    public Student(AcademicStream academicStream, AcademicGroup academicGroup, Education education, String firstName, String patronymicName, String surname, Calendar birthday, Sex sex, Citizenship citizenship, String handle, Passport passport, Calendar datePhliugraphia, String notes) {
        super(firstName, patronymicName, surname, birthday, sex, citizenship, handle, passport, datePhliugraphia, notes);
        this.academicStream = academicStream;
        this.academicGroup = academicGroup;
        this.education = education;
    }

    @ManyToOne
    public AcademicGroup getAcademicGroup() {
        return academicGroup;
    }

    public void setAcademicGroup(AcademicGroup academicGroup) {
        this.academicGroup = academicGroup;
    }

    @ManyToOne
    public AcademicStream getAcademicStream() {
        return academicStream;
    }

    public void setAcademicStream(AcademicStream academicStream) {
        this.academicStream = academicStream;
    }

    @ManyToOne
    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    @OneToMany
    public Set<EducationStudent> getEducationStudent() {
        return educationStudent;
    }

    public void setEducationStudent(Set<EducationStudent> educationStudent) {
        this.educationStudent = educationStudent;
    }
}
