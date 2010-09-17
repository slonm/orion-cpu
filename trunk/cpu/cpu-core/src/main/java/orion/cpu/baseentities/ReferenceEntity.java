package orion.cpu.baseentities;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.NonVisual;

/**
 * Базовая абстрактная сущность справочника.
 * Для Hibernate это MappedSuperclass.
 * Содержит поля базовой таблицы Reference
 * @param <T> Справочник
 * @author sl
 */
@MappedSuperclass
public abstract class ReferenceEntity<T extends ReferenceEntity<?>> extends NamedEntity<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String shortName;
    private Boolean isObsolete = false;
    @NonVisual
    private T aliasTo;
    /**
     * Внутреннее наименование записи. Используется как имя константы.
     */
    @NonVisual
    private String key;

    public T getAliasTo() {
        return aliasTo;
    }

    /**
     * @param aliasTo 
     * @throws ClassCastException если тип aliasTo не совпалает с типом this
     * @author sl
     */
    public void setAliasTo(T aliasTo) {
        T aliasToOld = aliasTo;
        if (aliasTo == null || aliasTo.equals(this) || aliasTo.getAliasTo() != null) {
            this.aliasTo = null;
        } else {
            if (!this.getClass().isAssignableFrom(aliasTo.getClass())) {
                throw new ClassCastException("Unassignable type: " + aliasTo.getClass().getName() + " -> " + this.getClass().getName());
            }
            this.aliasTo = aliasTo;
        }
    }

    public Boolean getIsObsolete() {
        return isObsolete;
    }

    public void setIsObsolete(Boolean isObsolete) {
        this.isObsolete = isObsolete;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Column(unique = true)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean entityEquals(T obj) {
        return aEqualsField(shortName, obj.getShortName()) &&
               aEqualsField(isObsolete, obj.getIsObsolete()) &&
               aEqualsField(aliasTo, obj.getAliasTo());
    }
}
