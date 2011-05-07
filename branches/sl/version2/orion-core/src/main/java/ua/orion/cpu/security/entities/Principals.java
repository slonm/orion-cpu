package ua.orion.cpu.security.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Сввязь между primaryPrincipal и субъектом.
 * Субъектом может быть персона, сервис и т.д
 * @author sl
 */
@Entity
@Table(schema = "sec")
public class Principals extends AbstractEntity<Principals> {

    private static final long serialVersionUID = 1L;
    @NotNull
    @Size(min = 1)
    @Column(unique = true)
    private String primaryPrincipal;
//    @ElementCollection
//    @CollectionTable(schema = "sec")
//    @Size(min = 1)
//    private Set<String> principals = new HashSet<String>();

    public String getPrimaryPrincipal() {
        return primaryPrincipal;
    }

    public void setPrimaryPrincipal(String primaryPrincipal) {
        this.primaryPrincipal = primaryPrincipal;
    }

//    public Set<String> getPrincipals() {
//        return principals;
//    }
//
//    public void setPrincipals(Set<String> principals) {
//        this.principals = principals;
//    }

    @Override
    protected boolean entityEquals(Principals obj) {
        return aEqualsField(primaryPrincipal, obj.primaryPrincipal);
    }

    @Override
    public int compareTo(Principals o) {
        return o == null || o.getPrimaryPrincipal() == null ? -1 : primaryPrincipal.compareToIgnoreCase(o.primaryPrincipal);
    }

    @Override
    public String toString() {
        return primaryPrincipal;
    }
}
