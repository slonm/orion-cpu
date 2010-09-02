package orion.cpu.baseentities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.validator.*;

/**
 *
 * @author sl
 */
@MappedSuperclass
public abstract class NamedEntity<T extends NamedEntity<?>> extends BaseEntity<T> {

    private String name;

    /**
     * Returns the value of the <code>name</code> property.
     *
     * @return a {@link String}.
     */
    @Column(nullable = false)
    @NotNull
    @Length(min = 2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(T o) {
        return name.compareToIgnoreCase(o.getName());
    }

    @Override
    protected boolean entityEquals(T obj) {
        return aEqualsField(name, obj.getName());
    }

    @Override
    public void aHashCode() {
        super.aHashCode();
        aHashField(getName());
    }

    @Override
    public String toString() {
        return name;
    }

}