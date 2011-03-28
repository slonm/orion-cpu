package orion.cpu.entities.org;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import orion.cpu.baseentities.NamedEntity;

/**
 * Базовое абстрактная единица организационной структуры
 * @author sl
 */
@Entity
@Table(schema = "org", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "shortName")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrgUnit extends NamedEntity<OrgUnit> {

    private static final long serialVersionUID = 1L;
    private OrgUnit parent;
    private String shortName;

    @Size(min = 1)
    @NotNull
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @ManyToOne
    public OrgUnit getParent() {
        return parent;
    }

    public void setParent(OrgUnit parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    protected boolean entityEquals(OrgUnit obj) {
        return super.entityEquals(obj) && aEqualsField(shortName, obj.shortName);
    }
}
