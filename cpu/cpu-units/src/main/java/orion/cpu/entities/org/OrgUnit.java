package orion.cpu.entities.org;

import javax.persistence.*;
import javax.validation.constraints.Size;
import orion.cpu.baseentities.NamedEntity;

/**
 * Базовое абстрактное структурное подразделение.
 * Для Hibernate это MappedSuperclass.
 * Содержит поля базовой таблицы Reference
 * @author sl
 */
@Entity
@Table(schema = "org", uniqueConstraints =
@UniqueConstraint(columnNames = "name"))
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrgUnit extends NamedEntity<OrgUnit> {

    private static final long serialVersionUID = 1L;
    @Size(min = 1)
    private String shortName;

    @Column(unique = true)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
