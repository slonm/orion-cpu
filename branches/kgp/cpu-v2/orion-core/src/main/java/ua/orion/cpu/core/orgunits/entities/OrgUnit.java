package ua.orion.cpu.core.orgunits.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Базовое абстрактная структурная единица.
 * @author sl
 */
@Entity
@Table(schema = "org")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrgUnit<T extends OrgUnit<?>> extends AbstractEnumerationEntity<T> {

    private static final long serialVersionUID = 1L;
    private OrgUnit parent;
    private String shortName;
    //список должностей подразделения с долей ставки для каждого экземпляра 
    //должности подразделения (штатное расписание)
    private Set<OrgUnitPost> orgUnitPosts = new HashSet<OrgUnitPost>();

    @Size(min = 2)
    @NotNull
    @Column(unique=true)
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

    @OneToMany(cascade=CascadeType.ALL, mappedBy="orgUnit")
    public Set<OrgUnitPost> getOrgUnitPosts() {
        return orgUnitPosts;
    }

    public void setOrgUnitPosts(Set<OrgUnitPost> orgUnitPosts) {
        this.orgUnitPosts = orgUnitPosts;
    }
    
    @Override
    public String toString() {
        return getName();
    }

    @Override
    protected boolean entityEquals(T obj) {
        return super.entityEquals(obj) && aEqualsField(shortName, obj.getShortName());
    }
}
