package ua.orion.cpu.core.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Подсистемы ИС
 * @author sl
 */
@Entity
@Table(schema = "sys")
public class SubSystem extends AbstractEnumerationEntity<SubSystem>{

    public SubSystem() {
    }

    public SubSystem(String name) {
        setName(name);
    }

}
