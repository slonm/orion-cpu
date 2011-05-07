package ua.orion.core.entities;

import java.io.Serializable;
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
public class SerializableSingleton extends AbstractEnumerationEntity<SerializableSingleton>
        implements PersistentSingleton<Serializable> {

    private Serializable singleton;

    @NotNull
    @Override
    public Serializable getSingleton() {
        return singleton;
    }

    @Override
    public void setSingleton(Serializable singleton) {
        this.singleton = singleton;
    }

    @Transient
    @Override
    public Class<Serializable> getSingletonClass() {
        return Serializable.class;
    }
}
