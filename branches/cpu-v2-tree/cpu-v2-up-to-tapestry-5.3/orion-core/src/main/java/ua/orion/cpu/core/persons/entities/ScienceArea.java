package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочник областей наук для учёных степеней
 * @author kgp
 */
@Entity
@ReferenceBook
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class ScienceArea extends AbstractReferenceEntity<ScienceArea> {

    private static final long serialVersionUID = 1L;

    public ScienceArea() {
    }

    public ScienceArea(String name, String shortName) {
        setName(name);
        setShortName(shortName);
    }
}
