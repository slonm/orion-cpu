package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - состав семьи
 *
 * @author molodec
 */
@Entity
@ReferenceBook
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
