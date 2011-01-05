package ua.orion.core.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ua.orion.core.persistence.AbstractEnumerationEntity;
import ua.orion.core.persistence.PersistentSingleton;

/**
 * Хранимые переменные в формате пар (ключ, значение)
 * Ключ - String
 * Значение - Serializable
 * @author sl
 */
@Entity
@Table(schema = "sys")
public class StringSingleton extends AbstractEnumerationEntity<StringSingleton>
        implements PersistentSingleton<String> {

    private String singleton;

    @NotNull
    @Column(length = 1000)
    @Override
    public String getSingleton() {
        return singleton;
    }

    @Override
    public void setSingleton(String singleton) {
        this.singleton = singleton;
    }

    @Transient
    @Override
    public Class<String> getSingletonClass() {
        return String.class;
    }
}
