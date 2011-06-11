package ua.orion.cpu.core.employees.entities;



import ua.orion.cpu.core.persons.entities.Person;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;

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
