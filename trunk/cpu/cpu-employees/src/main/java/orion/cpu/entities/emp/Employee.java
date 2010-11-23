package orion.cpu.entities.emp;

import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;
import orion.cpu.entities.psn.Person;

/**
 * Работник.
 * @author sl
 */
@Entity
@Table(schema = "emp")
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Person {
    private static final long serialVersionUID = 1L;

    @Size(min=1)
    @OneToMany(cascade=CascadeType.ALL, mappedBy="employee")
    private Set<EmployeePost> employeePosts;

}
