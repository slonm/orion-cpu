package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.AbstractReferenceEntity;

/**
 * Класс-справочник названий должностей сотрудников
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
public class Post extends AbstractReferenceEntity<Post> {

   private static final long serialVersionUID = 1L;
   
   public Post() {
    }

    public Post(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }
}
