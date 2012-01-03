package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочник учёных званий
 * @author kgp
 */
@Entity
@ReferenceBook
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class AcademicRank extends AbstractReferenceEntity<AcademicRank> {

    private static final long serialVersionUID = 1L;

    public AcademicRank() {
    }
    
    public AcademicRank(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
}