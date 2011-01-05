package ua.orion.cpu.persons.entities;



import javax.persistence.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * @author sl
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class AcademicRank extends AbstractReferenceEntity<AcademicRank> {

    private static final long serialVersionUID = 1L;
}
