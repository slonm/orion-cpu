package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;

/**
 * Класс-сущность кафедра
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Chair extends OrgUnit<Chair> {
    
    private static final long serialVersionUID = 1L;
    
    public Chair() {
    }
    
    public Chair(String name, String shortName, OrgUnit orgUnit) {
        setName(name);
        setShortName(shortName);
        //Устанавливаем OrgUnit, к которому закреплена кафедра
        setParent(orgUnit);
    }
}
