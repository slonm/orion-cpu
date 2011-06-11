package ua.orion.core.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.NonVisual;

/**
 * Базовая абстрактная сущность.
 * Содержит только поле id
 * @author sl
 */
@MappedSuperclass
public abstract class AbstractEntity<T extends AbstractEntity<?>>
        implements Serializable, Comparable<T>, Cloneable, IEntity {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Timestamp lastChange;

    @Version
    @NonVisual
    public Timestamp getLastChange() {
        return lastChange;
    }

    public void setLastChange(Timestamp lastChange) {
        this.lastChange = lastChange;
    }

    /**
     * Returns the value of the <code>id</code> property.
     * @return a {@link Integer}.
     */
    @Id
    @GeneratedValue
    @NonVisual
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Пример переопределения:
     * z - примитивного типа
     * <pre>
     * public boolean aEquals(Sub obj){
     *      return super.aEquals(obj)&&
     *             aEqualsField(f1, obj.f1)&&
     *             aEqualsField(f2, obj.f2)&&
     *             z==other.z;
     * }
     * </pre>
     * @param obj 
     * @return
     */
    //1) Эквивалентность id
    //2) Если оба id==null то эквивалентность всех других полей
    final public boolean aEquals(Object obj) {
        AbstractEntity<?> cast = (AbstractEntity<?>) obj;
        if (id == null && cast.id == null) {
            return entityEquals((T) obj);
        }
        return aEqualsField(id, cast.id);
    }

    protected abstract boolean entityEquals(T obj);

    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException ex) {
        }
        throw new RuntimeException();
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("id=").append(id);
        return buf.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return aEquals(obj);
    }

    /**
     *
     * @param <O>
     * @param fieldObj1
     * @param fieldObj2
     * @return
     */
    public final <O> boolean aEqualsField(O fieldObj1, O fieldObj2) {
        return (fieldObj1 == null) ? false : fieldObj1.equals(fieldObj2);
    }

    @Override
    public int hashCode() {
        if (id==null) {
            return System.identityHashCode(this);
        } else {
            return id;
        }
    }
}
