package orion.cpu.entities.ref;

import javax.persistence.*;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * @author sl
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class AcademicRank extends ReferenceEntity<AcademicRank> {

    private static final long serialVersionUID = 1L;
}
