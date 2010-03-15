package orion.cpu.baseentities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.NonVisual;
import ua.mihailslobodyanuk.base.AClonable;
import ua.mihailslobodyanuk.base.AObject;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Базовая абстрактная сущность.
 * Содержит только поле id
 * @author sl
 */
@MappedSuperclass
public abstract class BaseEntity<T extends BaseEntity<?>> extends AObject
        implements Serializable, Comparable<T>, AClonable<T> {

    private static final long serialVersionUID = 1L;
    @NonVisual
    private Integer id;
    @NonVisual
    private EntityAudit audit;

    public BaseEntity() {
        super(13, 76);
    }

    protected void auditRecord() {
        getAudit().record();
    }

    protected <O> void auditRecord(O obj1, O obj2) {
        if (!aEqualsField(obj1, obj2)) {
            auditRecord();
        }
    }

    /**
     * Returns the value of the <code>id</code> property.
     * @return a {@link Integer}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        auditRecord(this.id, id);
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EntityAudit getAudit() {
        if (audit == null) {
            audit = EntityAudit.create();
        }
        return audit;
    }

    public void setAudit(EntityAudit audit) {
        this.audit = audit;
    }

    //1) Эквивалентность id
    //2) Если оба id==null то эквивалентность всех других полей
    @Override
    final public boolean aEquals(Object obj) {
        BaseEntity<?> cast = (BaseEntity<?>) obj;
        if (id == null && cast.id == null) {
            return entityEquals((T)obj);
        }
        return aEqualsField(id, cast.id);
    }

    protected abstract boolean entityEquals(T obj);

    @Override
    public void aHashCode() {
        aHashField(id);
    }

    @Override
    public void aToString() {
        aToStringBegin(BaseEntity.class);
        aToStringField("id", getId(), true);
        aToStringEnd();
    }

    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException ex) {
        }
        throw new RuntimeException("Clone Not Supported on "+getClass().getName());
    }

    public static String getSchema(Class<?> entityClass) {
        Defense.notNull(entityClass, "entityClass");
        String[] path = entityClass.getName().split("\\.");
        if (path.length >= 2) {
            return path[path.length - 2];
        }
        return "";
    }

    public static String getFullClassName(Class<?> entityClass) {
        Defense.notNull(entityClass, "entityClass");
        String schema = getSchema(entityClass);
        return (schema.length() != 0 ? schema + "." : "") + entityClass.getSimpleName();
    }

}
