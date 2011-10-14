package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Справочник учёных степней
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class ScientificDegree extends AbstractReferenceEntity<ScientificDegree> {

    private static final long serialVersionUID = 1L;
    
    public ScientificDegree() {
    }

    public ScientificDegree(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
}
