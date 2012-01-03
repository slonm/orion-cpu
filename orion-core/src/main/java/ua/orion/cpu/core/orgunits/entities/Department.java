package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Класс-сущность отдел
 * @author kgp
 */
@Entity
public class Department extends OrgUnit<Department> {

    private static final long serialVersionUID = 1L;
    
    public Department() {
    }

    public Department(String name, String shortName, OrgUnit orgUnit) {
        setName(name);
        setShortName(shortName);
        //Устанавливаем OrgUnit, к которому закреплен отдел
        setParent(orgUnit);
    }
}
