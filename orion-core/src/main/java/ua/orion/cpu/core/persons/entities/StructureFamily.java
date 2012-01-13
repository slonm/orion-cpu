package ua.orion.cpu.core.persons.entities;

import java.util.Calendar;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Стуктура семьи. Хранит информацию о члене семьи и связку его с персоной.
 *
 * @author molodec
 */
@Entity
public class StructureFamily extends AbstractEntity<StructureFamily> {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Персона, к семье которой относится данный человек
     */
    private Person person;
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
     * Родство
     */
    private Cognation cognation;
    /**
     * Дата рождения
     */
    private Calendar birthDay;
    /**
     * Место работы
     */
    private String placeActivity;
    /**
     * Должность
     */
    private String post;
    /**
     * Номер телефона
     */
    private String phoneNumber;

    /**
     * Метод создания члена семьи по умолчанию
     */
    public StructureFamily() {
    }

    /**
     * Метод создания члена семьи на основе всех полей, а именно:
     *
     * @param person - персона
     * @param firstName - имя
     * @param patronymicName - отчество
     * @param surname - фамилия
     * @param cognation - родство
     * @param birthDay - дата рождения
     * @param placeActivity - место работы
     * @param post - должность
     * @param phoneNumber - номер телефона
     */
    public StructureFamily(Person person, String firstName, String patronymicName,
            String surname, Cognation cognation, Calendar birthDay, String placeActivity,
            String post, String phoneNumber) {
        this.person = person;
        this.firstName = firstName;
        this.patronymicName = patronymicName;
        this.surname = surname;
        this.cognation = cognation;
        this.birthDay = birthDay;
        this.placeActivity = placeActivity;
        this.post = post;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Получение персоны, у которой в семье есть данный человек
     *
     * @return
     */
    @ManyToOne
    public Person getPerson() {
        return person;
    }

    /**
     * Установка персоны
     *
     * @param person - персона
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Получение имени человека
     *
     * @return - строка, содержащая имя
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Установка имени
     *
     * @param firstName - имя
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Получение отчества человека
     *
     * @return отчество
     */
    public String getPatronymicName() {
        return patronymicName;
    }

    /**
     * Установка отчества
     *
     * @param patronymicName
     */
    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    /**
     * Получение фамилии
     *
     * @return фамилия
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Установка фамилии
     *
     * @param surname - фамилия
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Получение даты рождения
     *
     * @return объект даты, содержащий информацию о дате рождения
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getBirthDay() {
        return birthDay;
    }

    /**
     * Установк даты рождения
     *
     * @param birthDay - объект даты
     */
    public void setBirthDay(Calendar birthDay) {
        this.birthDay = birthDay;
    }

    /**
     * Родственность
     *
     * @return родственость
     */
    @ManyToOne
    public Cognation getCognation() {
        return cognation;
    }

    /**
     * Установка родственности
     *
     * @param cognation
     */
    public void setCognation(Cognation cognation) {
        this.cognation = cognation;
    }

    /**
     * Получение номера телефона
     *
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Установка номера телефона
     *
     * @param phoneNumber - номер телефона
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Получение места работы
     *
     * @return место работы
     */
    public String getPlaceActivity() {
        return placeActivity;
    }

    /**
     * Установка места работы
     *
     * @param placeActivity - место работы
     */
    public void setPlaceActivity(String placeActivity) {
        this.placeActivity = placeActivity;
    }

    /**
     * Получение должности
     *
     * @return строка с информцией о должности
     */
    public String getPost() {
        return post;
    }

    /**
     * Установка должности
     *
     * @param post - строка с информацией о должности
     */
    public void setPost(String post) {
        this.post = post;
    }

    @Override
    protected boolean entityEquals(StructureFamily obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(StructureFamily o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
