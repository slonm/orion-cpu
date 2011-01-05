package ua.orion.cpu.eduprocess.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Сущность-справочник названий дисциплин
 * @author kgp
 */
@Entity
@Table(schema = "ref", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})
})
public class Discipline extends AbstractReferenceEntity<Discipline> {

    private static final long serialVersionUID = 1L;

    public Discipline() {
    }

    public Discipline(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
}
