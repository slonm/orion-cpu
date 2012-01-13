package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - города. Хранит список городов
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class City extends AbstractEntity<City> {

    private static final long serialVersionUID = 1L;
    @NotNull
    private String name;
    private Country country;
    private Region region;
    private District district;
    @DataType("longtext")
    private String addinitionalData;
    private NatureResidence status;
    private City parent;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public City(String name, Country country, Region region, District district, NatureResidence status, String addinitionalData, City parent) {
        this.name = name;
        this.country = country;
        this.region = region;
        this.district = district;
        this.status = status;
        this.addinitionalData = addinitionalData;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    public NatureResidence getStatus() {
        return status;
    }

    public void setStatus(NatureResidence status) {
        this.status = status;
    }

    @ManyToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @ManyToOne
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @ManyToOne
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @OneToOne
    public City getParent() {
        return parent;
    }

    public void setParent(City parent) {
        this.parent = parent;
    }

    /**
     * Дополнительная информация о городе Данное поле хранит все то, что
     * относится к области, но не относится ни к одному другому полю данной
     * сущности
     *
     * @return
     */
    public String getAddinitionalData() {
        return addinitionalData;
    }

    public void setAddinitionalData(String addinitionalData) {
        this.addinitionalData = addinitionalData;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected boolean entityEquals(City obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(City o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
