package ua.orion.cpu.orgunits.entities;



import javax.persistence.*;

/**
 * Класс-сущность отдел
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Department extends OrgUnit<Department> {

   private static final long serialVersionUID = 1L;
}
