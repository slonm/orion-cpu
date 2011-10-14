package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Класс-сущность институт
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Institute extends OrgUnit<Institute> {

    private static final long serialVersionUID = 1L;

    public Institute() {
    }
    
    public Institute(String name, String shortName, OrgUnit orgUnit) {
        setName(name);
        setShortName(shortName);
        //Устанавливаем OrgUnit, к которому закреплен институт
        setParent(orgUnit);
    }
}
