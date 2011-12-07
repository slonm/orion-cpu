package ua.orion.cpu.core.persons.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Справочный класс - гражданство
 *
 * @author molodec
 */
@Entity
@Table(schema = "ref")
public class Citizenship extends AbstractEnumerationEntity<Citizenship> {

    private static final long serialVersionUID = 1L;
    private Country country;

    public Citizenship(String name) {
        setName(name);
    }

    public Citizenship(String name, Country country) {
        setName(name);
        this.country = country;
    }

    public Citizenship() {
    }

    @OneToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return getName();
    }
}
