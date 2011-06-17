package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;

/**
 * Класс-сущность факультет
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class Faculty extends OrgUnit<Faculty> {

   private static final long serialVersionUID = 1L;
}
