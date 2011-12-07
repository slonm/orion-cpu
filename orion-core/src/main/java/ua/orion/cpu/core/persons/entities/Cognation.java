package ua.orion.cpu.core.persons.entities;

import ua.orion.cpu.core.persons.entities.*;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Справочный класс - состав семьи
 *
 * @author molodec
 */
@Entity
@Table(schema = "ref")
public class Cognation extends AbstractEnumerationEntity<Cognation> {

    private static final long serialVersionUID = 1L;
    private Boolean isChild;

    public Cognation(String name, Boolean isChild) {
        setName(name);
        this.isChild = isChild;
    }

    public Cognation() {
    }

    public Boolean getIsChild() {
        return isChild;
    }

    public void setIsChild(Boolean isChild) {
        this.isChild = isChild;
    }
}
