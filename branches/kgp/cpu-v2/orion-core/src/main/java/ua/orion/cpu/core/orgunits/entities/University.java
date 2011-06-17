package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;

/**
 * Класс-сущность университет - скорее всего корень организационной иерархии
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class University extends OrgUnit<University> {

    private static final long serialVersionUID = 1L;
//TODO Продумать как ввести университет КПУ без ссылки на родительское подразделение, чтоб избежать orgUnit is null
}
