/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.entities.org;

import orion.cpu.entities.*;
import javax.persistence.*;

/**
 * Класс-сущность университет - скорее всего корень организационной иерархии
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class University extends OrgUnit {

    private static final long serialVersionUID = 1L;
//TODO Продумать как ввести университет КПУ без ссылки на родительское подразделение, чтоб избежать orgUnit is null
}
