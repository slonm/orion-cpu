package ua.orion.core.persistence;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Сущность с атрибутом name
 * @author sl
 */
@MappedSuperclass
public abstract class AbstractNamedEntity<T extends AbstractNamedEntity<?>> extends AbstractEntity<T> {

    private String name;

    @NotNull
    @Size(min = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(T o) {
        return o==null?-1:name.compareToIgnoreCase(o.getName());
    }

    @Override
    protected boolean entityEquals(T obj) {
        return obj==null?false:aEqualsField(name, obj.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
