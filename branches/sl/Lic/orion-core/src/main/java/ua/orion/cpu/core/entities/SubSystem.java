package ua.orion.cpu.core.entities;

import javax.persistence.Entity;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Подсистемы ИС
 * @author sl
 */
@Entity
public class SubSystem extends AbstractEnumerationEntity<SubSystem>{

    public SubSystem() {
    }

    public SubSystem(String name) {
        setName(name);
    }

}
