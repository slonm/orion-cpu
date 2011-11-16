package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ua.orion.core.persistence.AbstractNamedEntity;

/**
 * @author sl
 */
@Entity
@Table(schema = "psn")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends AbstractNamedEntity<Person> {

    private static final long serialVersionUID = 1L;
    private String firstName;
    private String patronymicName;
    private String surname;
    private ScientificDegree scientificDegree;
    private ScienceArea scienceArea;
    private AcademicRank academicRank;

    public Person() {
    }

    public Person(String firstName, String patronymicName, String surname, ScientificDegree scientificDegree, ScienceArea scienceArea, AcademicRank academicRank) {
        this.firstName = firstName;
        this.patronymicName = patronymicName;
        this.surname = surname;
        this.scientificDegree = scientificDegree;
        this.scienceArea = scienceArea;
        this.academicRank = academicRank;
    }

    @Size(min = 2)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = capitalize(firstName);
    }

    @Size(min = 2)
    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = capitalize(patronymicName);
    }

    @Size(min = 2)
    @NotNull
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = capitalize(surname);
    }

    /*TODO Добавить валидатор, указывающий на недопустимость пустого значения scientificDegree 
     *при непустом scienceArea и наоборот пустого scienceArea при непустом scientificDegree
     *if(!scientificDegree.equals(null) ^ !(scienceArea)) {нельзя!!!}
     */
    @ManyToOne
    public ScientificDegree getScientificDegree() {
        return scientificDegree;
    }

    public void setScientificDegree(ScientificDegree scientificDegree) {
        this.scientificDegree = scientificDegree;
    }

    @ManyToOne
    public ScienceArea getScienceArea() {
        return scienceArea;
    }

    public void setScienceArea(ScienceArea scienceArea) {
        this.scienceArea = scienceArea;
    }

    @ManyToOne
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
        sb.append(surname);
        sb.append(" ");
        sb.append(firstName);
        if (patronymicName != null) {
            sb.append(" ");
            sb.append(patronymicName);
        }
        return sb.toString();
    }

    @Transient
    public String getInitialsNameRu() {
        StringBuilder sb = new StringBuilder();
        sb.append(surname);
        sb.append(" ");
        sb.append(firstName.substring(0, 1));
        sb.append(".");
        if (patronymicName != null) {
            sb.append(patronymicName.substring(0, 1));
            sb.append(".");
        }
        return sb.toString();
    }

    @Transient
    public String getFullNameEn() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        sb.append(" ");
        sb.append(surname);
        return sb.toString();
    }

    @Transient
    public String getSciDegreeSciAreaFull() {
        if (scientificDegree != null && scienceArea != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(scientificDegree);
            sb.append(" ");
            sb.append(scienceArea);
            return sb.toString();
        }
        return "";
    }

    @Transient
    public String getSciDegreeSciAreaShort() {
        try {
            if (scientificDegree.getShortName() != null && scienceArea.getShortName() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(scientificDegree.getShortName());
                sb.append(scienceArea.getShortName());
                return sb.toString();
            }
        } catch (Exception e) {
        }
        return "";
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
