package ua.orion.cpu.core.persons.entities;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Справочный класс - бывшие фамилии человека. Содержит обязательные поля - пер-
 * сона, которая носила данную фамилию, сама фамилия и дата, с которой человеку
 * была присвоена эта фамилия.
 *
 * @author molodec
 */
@Entity
@Table(schema = "psn")
public class FormerSurname extends AbstractEntity<FormerSurname> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    @NotNull
    private Person person;
    @NotNull
    private String surname;
    @NotNull
    private Calendar dateBegin;

    /**
     * Создание бывшей фамилии на основе трех обязательных полей
     *
     * @param person - персона
     * @param surname - фамилия
     * @param date - дата с которой персона носит данную фамилию
     */
    public FormerSurname(Person person, String surname, Calendar date) {
        this.person = person;
        this.surname = surname;
        this.dateBegin = date;
    }

    /**
     * При создании фамилии через конструктор по умолчанию необходимо будет
     * установить значения в поля person, surname, dateBegin с помощью set ме-
     * тодов, доступных в данном классе.
     */
    public FormerSurname() {
    }

    /**
     * Дата начала действия фамилии. То есть дата, с которой персоне была
     * присвоена данная фамилия.
     *
     * @return дата
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDate() {
        return dateBegin;
    }

    public void setDate(Calendar date) {
        this.dateBegin = date;
    }

    /**
     * Персона, которая носит/носила данную фамилию.
     *
     * @return
     */
    @ManyToOne
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Сама фамилия - текстовоя строка
     *
     * @return
     */
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    protected boolean entityEquals(FormerSurname obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(FormerSurname o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
