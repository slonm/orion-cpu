package orion.cpu.entities.sys;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import orion.cpu.baseentities.NamedEntity;

/**
 * Подсистемы ИС
 * @author sl
 */
@Entity
@Table(schema = "sys", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
public class SubSystem extends NamedEntity<SubSystem>{

    public SubSystem() {
    }

    public SubSystem(String name) {
        setName(name);
    }

}
