/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.cpu.entities.pub;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import orion.cpu.baseentities.NamedEntity;

/**
 *
 * @author sl
 */
@Entity
@Table(schema="pub")
@Inheritance(strategy=InheritanceType.JOINED)
public class A extends NamedEntity<A>{

    private static final long serialVersionUID = 1L;
    public A() {
    }
    
    public A(String name) {
        setName(name);
    }
}
