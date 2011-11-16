package ua.orion.cpu.core.students.entities;



import ua.orion.cpu.core.persons.entities.Person;
import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Студенты.
 * @author sl
 */
@Entity
@Table(schema = "stu")
public class Student extends AbstractEntity<Student> {
    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne
    private Person person;
    
    @NotNull
    @ManyToOne
    private AcademicStream academicStream;

    @NotNull
    @ManyToOne
    private AcademicGroup academicGroup;

    public Student() {
    }

    public Student(Person person, AcademicStream academicStream, AcademicGroup academicGroup) {
        this.person = person;
        this.academicStream = academicStream;
        this.academicGroup = academicGroup;
    }
    
    public AcademicGroup getAcademicGroup() {
        return academicGroup;
    }

    public void setAcademicGroup(AcademicGroup academicGroup) {
        this.academicGroup = academicGroup;
    }

    public AcademicStream getAcademicStream() {
        return academicStream;
    }

    public void setAcademicStream(AcademicStream academicStream) {
        this.academicStream = academicStream;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    protected boolean entityEquals(Student obj) {
        return aEqualsField(person, obj.person)
                && aEqualsField(academicStream, obj.academicStream)
                && aEqualsField(academicGroup, obj.academicGroup);
    }

    @Override
    public int compareTo(Student o) {
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }

    @Override
    public String toString() {
        return "Student{" + "person=" + person + ", academicStream=" + academicStream + ", academicGroup=" + academicGroup + '}';
    }

}
