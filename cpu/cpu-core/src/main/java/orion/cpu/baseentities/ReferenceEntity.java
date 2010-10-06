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
    /**
     * Внутреннее наименование записи. Используется как имя константы.
     */
    @NonVisual
    private String key;

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
               aEqualsField(isObsolete, obj.getIsObsolete());
    }
}
