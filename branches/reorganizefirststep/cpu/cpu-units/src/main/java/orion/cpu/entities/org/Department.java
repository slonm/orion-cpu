package orion.cpu.entities.org;

import orion.cpu.entities.*;
import javax.persistence.*;

/**
 * Класс-сущность отдел
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Department extends OrgUnit {

   private static final long serialVersionUID = 1L;
}
