package orion.cpu.baseentities;

import br.com.arsmachina.authentication.entity.User;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.NonVisual;
import ua.mihailslobodyanuk.base.AClonable;
import ua.mihailslobodyanuk.base.AObject;
import ua.mihailslobodyanuk.utils.Defense;
import orion.cpu.security.OperationTypes;
import orion.cpu.utils.PossibleOperations;

/**
 * Базовая абстрактная сущность.
 * Содержит только поле id
 * @author sl
 */
@MappedSuperclass
@PossibleOperations({OperationTypes.READ_OP, OperationTypes.STORE_OP,
OperationTypes.UPDATE_OP, OperationTypes.REMOVE_OP, OperationTypes.MENU_OP})
public abstract class BaseEntity<T extends BaseEntity<?>> extends AObject
        implements Serializable, Comparable<T>, AClonable<T> {

    private static final long serialVersionUID = 1L;
    @NonVisual
    private Integer id;
    @NonVisual
    private String filler;
    @NonVisual
    private Timestamp fillDateTime;
    @NonVisual
    private Timestamp modifyDateTime;

    public BaseEntity() {
        super(13, 76);
    }

    /**
     * Returns the value of the <code>id</code> property.
     * @return a {@link Integer}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getFillDateTime() {
        return fillDateTime;
    }

    public String getFiller() {
        return filler;
    }

    @Version
    public Timestamp getModifyDateTime() {
        return modifyDateTime;
    }

    public void setFillDateTime(Timestamp fillDateTime) {
        this.fillDateTime = fillDateTime;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public void setModifyDateTime(Timestamp modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    //1) Эквивалентность id
    //2) Если оба id==null то эквивалентность всех других полей
    @Override
    final public boolean aEquals(Object obj) {
        BaseEntity<?> cast = (BaseEntity<?>) obj;
        if (id == null && cast.id == null) {
            return entityEquals((T) obj);
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
        throw new RuntimeException("Clone Not Supported on " + getClass().getName());
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
