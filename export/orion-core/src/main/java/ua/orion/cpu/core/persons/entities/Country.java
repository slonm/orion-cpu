package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - страна Хранит список стран Не наследует
 * AbstractNamedEntity так как нет необходимости в поле "Устарело"
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class Country extends AbstractEnumerationEntity<Country> {

    private static final long serialVersionUID = 1L;
    private String officialLanguage;
    private String capital;
    private String formOfGovernment;
    private String currency;
    @DataType("longtext")
    private String addinitionalData;

    public Country() {
    }

    public Country(String name) {
        setName(name);
    }

    public Country(String name, String officialLanguage, String capital, String formOfGovernment, String currency, String addinitionalData) {
        setName(name);
        this.officialLanguage = officialLanguage;
        this.capital = capital;
        this.formOfGovernment = formOfGovernment;
        this.currency = currency;
        this.addinitionalData = addinitionalData;
    }

    /**
     * Столица
     *
     * @return
     */
    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    /**
     * Денежная единица
     *
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Форма правления
     *
     * @return
     */
    @Column(length=1000)
    public String getFormOfGovernment() {
        return formOfGovernment;
    }

    public void setFormOfGovernment(String formOfGovernment) {
        this.formOfGovernment = formOfGovernment;
    }

    /**
     * Оффициальный язык
     *
     * @return
     */
    public String getOfficialLanguage() {
        return officialLanguage;
    }

    public void setOfficialLanguage(String officialLanguage) {
        this.officialLanguage = officialLanguage;
    }

    /**
     * Дополнительная информация о стране Данное поле хранит все то, что
     * относится к странне, но не относится ни к одному другому полю данной
     * сущности
     *
     * @return
     */
    @Column(length=1000)
    public String getAddinitionalData() {
        return addinitionalData;
    }

    public void setAddinitionalData(String addinitionalData) {
        this.addinitionalData = addinitionalData;
    }

}
