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
    /**
     * Страна к которой относится данное гражданство
     */
    private Country country;

    /**
     * Создать гражданство на основе названия
     * @param name - названия
     */
    public Citizenship(String name) {
        setName(name);
    }

    /**
     * Создать гражданство на основе названия и страны
     * @param name - название
     * @param country - страна
     */
    public Citizenship(String name, Country country) {
        setName(name);
        this.country = country;
    }

    /**
     * Конструктор по умолчанию для создания гражданства
     */
    public Citizenship() {
    }

    /**
     * Получить страну, в которой действует данное гражданство
     * @return страна
     */
    @OneToOne
    public Country getCountry() {
        return country;
    }

    /**
     * Установить страну в которой действует данное гражданство
     * @param country - страна
     */
    public void setCountry(Country country) {
        this.country = country;
    }


}
