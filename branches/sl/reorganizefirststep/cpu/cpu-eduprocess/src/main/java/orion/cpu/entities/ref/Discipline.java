/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.entities.ref;

import javax.persistence.*;
import orion.cpu.baseentities.ReferenceEntity;

/**
 * Сущность-справочник названий дисциплин
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class Discipline extends ReferenceEntity<Discipline> {

    private static final long serialVersionUID = 1L;

    public Discipline() {
    }

    public Discipline(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
}
