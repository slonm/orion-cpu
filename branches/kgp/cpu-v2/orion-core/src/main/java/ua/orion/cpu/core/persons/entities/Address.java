package ua.orion.cpu.core.persons.entities;

import java.util.Calendar;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Справочный класс - пол человека
 *
 * @author molodec
 */
@Entity
@Table(schema = "psn")
public class Address extends AbstractEntity<Address> {

    private static final long serialVersionUID = 1L;
    private String index;
    private Country country;
    private Region region;
    private District district;
    @NotNull
    private City city;
    @NotNull
    private String address;
    @NotNull
    private Calendar dateBegin;
    private String homeTelephoneNumber;

    public Address() {
    }

    /**
     * Создает адрес на основе:
     *
     * @param index - почтовый индекс
     * @param country - страна
     * @param region - регион
     * @param district - область
     * @param city - город
     * @param address - адрес
     * @param dateBegin - дата начала жительства по данному адресу
     * @param homeTelephoneNumber - стационарный номер телефона
     */
    public Address(String index, Country country, Region region, District district, City city, String address, Calendar dateBegin, String homeTelephoneNumber) {
        this.index = index;
        this.country = country;
        this.region = region;
        this.district = district;
        this.city = city;
        this.address = address;
        this.dateBegin = dateBegin;
        this.homeTelephoneNumber = homeTelephoneNumber;
    }

    /**
     * Получить дату начала жительства по данному адресу
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Calendar dateBegin) {
        this.dateBegin = dateBegin;
    }

    /**
     * Страна
     *
     * @return
     */
    @ManyToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Регион
     *
     * @return
     */
    @ManyToOne
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Область
     *
     * @return
     */
    @ManyToOne
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    /**
     * Город
     *
     * @return
     */
    @ManyToOne
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    /**
     * Адрес - улица, номер дома, квартиры и т.д.
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Почтовый индекс
     *
     * @return
     */
    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * Номер стационарного телефона
     *
     * @return
     */
    public String getHomeTelephoneNumber() {
        return homeTelephoneNumber;
    }

    public void setHomeTelephoneNumber(String homeTelephoneNumber) {
        this.homeTelephoneNumber = homeTelephoneNumber;
    }

    @Override
    protected boolean entityEquals(Address obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Address o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
