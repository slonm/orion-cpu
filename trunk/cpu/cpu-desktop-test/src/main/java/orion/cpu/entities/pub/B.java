/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.entities.pub;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author sl
 */
@Entity
@Table(schema="pub")
public class B extends A {

    
    private B alias;

    public B getAlias() {
        return alias;
    }

    public void setAlias(B alias) {
        this.alias = alias;
    }

    public B alias(B alias) {
        this.alias = alias;
        return this;
    }

    public B() {
    }

    public B(String name) {
        setName(name);
    }
}
