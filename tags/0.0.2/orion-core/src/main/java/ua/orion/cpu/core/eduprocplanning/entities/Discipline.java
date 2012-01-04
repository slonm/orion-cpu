package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Справочник названий дисциплин
 * @author kgp
 */
@Entity
@Table(schema = "ref")
@AttributeOverrides({
    @AttributeOverride(name = "name", column =
    @Column(unique = true)),
    @AttributeOverride(name = "shortName", column =
    @Column(unique = true))})
@UserPresentable("name")
@Cacheable
public class Discipline extends AbstractReferenceEntity<Discipline> {

    private static final long serialVersionUID = 1L;

    public Discipline() {
    }

    public Discipline(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
}
