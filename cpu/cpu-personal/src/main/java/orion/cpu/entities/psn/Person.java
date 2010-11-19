package orion.cpu.entities.psn;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import orion.cpu.baseentities.NamedEntity;
import orion.cpu.entities.ref.AcademicDegree;
import orion.cpu.entities.ref.AcademicRank;

/**
 * @author sl
 */
@Entity
@Table(schema = "psn")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends NamedEntity<Person> {

    private static final long serialVersionUID = 1L;
    private String firstName;
    private String secondName;
    private String surename;
    private AcademicDegree academicDegree;
    private AcademicRank academicRank;

    @Size(min = 2)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = capitalize(firstName);
    }

    @Size(min = 2)
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = capitalize(secondName);
    }

    @Size(min = 2)
    @NotNull
    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = capitalize(surename);
    }

    public AcademicDegree getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(AcademicDegree academicDegree) {
        this.academicDegree = academicDegree;
    }

    public AcademicRank getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(AcademicRank academicRank) {
        this.academicRank = academicRank;
    }

    @Transient
    @Override
    public String getName() {
        return getFullNameRu();
    }

    @Override
    public void setName(String name) {
    }

    @Transient
    public String getFullNameRu() {
        StringBuilder sb = new StringBuilder();
        sb.append(surename);
        sb.append(" ");
        sb.append(firstName);
        if (secondName != null) {
            sb.append(" ");
            sb.append(secondName);
        }
        return sb.toString();
    }

    @Transient
    public String getInitialsNameRu() {
        StringBuilder sb = new StringBuilder();
        sb.append(surename);
        sb.append(" ");
        sb.append(firstName.substring(0, 1));
        sb.append(".");
        if (secondName != null) {
            sb.append(secondName.substring(0, 1));
            sb.append(".");
        }
        return sb.toString();
    }

    @Transient
    public String getFullNameEn() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        sb.append(" ");
        sb.append(surename);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFullNameRu();
    }

    //Недостаточно информации для проверки эквивалентности
    @Override
    protected boolean entityEquals(Person obj) {
        return this == obj;
    }

    private static String capitalize(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() < 2) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
    }
}
