package ua.orion.cpu.core.persons.entities;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.AbstractNamedEntity;

/**
 * Хранит базовую информацию о персоне.
 *
 * @author sl
 */
@Entity
@Table(schema = "psn")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends AbstractNamedEntity<Person> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Имя
     */
    private String firstName;
    /**
     * Отчество
     */
    private String patronymicName;
    /**
     * Фамилия
     */
    private String surname;
    /**
     * Дата рождения
     */
    private Calendar birthday;
    /**
     * Пол
     */
    private Sex sex;
    /**
     * Гражданство
     */
    private Citizenship citizenship;
    /**
     * ID
     */
    private String handle;
    /**
     * Пасспорт
     */
    private Passport passport;
    /**
     * Дата прохождения флюрографии
     */
    private Calendar datePhliugraphia;
    /**
     * Адреса
     */
    private Set<Address> address = new HashSet();
    /**
     * Заметки
     */
    private String notes;

    public Person() {
    }

    public Person(String firstName, String patronymicName, String surname, Calendar birthday, Sex sex, Citizenship citizenship, String handle, Passport passport, Calendar datePhliugraphia, String notes) {
        this.firstName = firstName;
        this.patronymicName = patronymicName;
        this.surname = surname;
        this.birthday = birthday;
        this.sex = sex;
        this.citizenship = citizenship;
        this.handle = handle;
        this.passport = passport;
        this.datePhliugraphia = datePhliugraphia;
        this.notes = notes;
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

    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    @NotNull
    @ManyToOne
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @ManyToOne
    public Citizenship getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }

    @OneToOne
    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDatePhliugraphia() {
        return datePhliugraphia;
    }

    public void setDatePhliugraphia(Calendar datePhliugraphia) {
        this.datePhliugraphia = datePhliugraphia;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @OneToMany
    public Set<Address> getAddress() {
        return address;
    }

    public void setAddress(Set<Address> address) {
        this.address = address;
    }

    @DataType("longText")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
