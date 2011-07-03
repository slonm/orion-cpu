package ua.orion.core.persistence;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.NonVisual;
import ua.orion.core.annotations.UniqueKey;

/**
 * Базовая абстрактная сущность справочника.
 * Для Hibernate это MappedSuperclass.
 * Содержит поля базовой таблицы Reference
 * @param <T> Справочник
 * @author sl
 */
@MappedSuperclass
public abstract class AbstractReferenceEntity<T extends AbstractReferenceEntity<?>> extends AbstractNamedEntity<T> {

    private static final long serialVersionUID = 1L;
    private String shortName;
    private Boolean isObsolete = false;
    /**
     * Внутреннее наименование записи. Используется как имя константы.
     */
    private String uKey;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getIsObsolete() {
        return isObsolete;
    }

    public void setIsObsolete(Boolean isObsolete) {
        this.isObsolete = isObsolete;
    }

    @Column(unique = true)
    @NonVisual
    @UniqueKey
    public String getUKey() {
        return uKey;
    }

    public void setUKey(String uKey) {
        this.uKey = uKey;
    }

    @Override
    public boolean entityEquals(T obj) {
        return aEqualsField(shortName, obj.getShortName()) &&
               aEqualsField(isObsolete, obj.getIsObsolete());
    }
}
