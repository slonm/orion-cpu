package ua.orion.cpu.entities;

import java.io.Serializable;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Хранимые в базе данных константы в формате пар (ключ, значение)
 * Ключ - String
 * Значение - Serializable
 * @author sl
 */
@Entity
@Table(schema = "sys")
public class StoredConstant extends AbstractEnumerationEntity<StoredConstant> {

    private Serializable constValue;

    /**
     * Get the value of constValue
     *
     * @return the value of constValue
     */
    @Column(length = 1000)
    public Serializable getConstValue() {
        return constValue;
    }

    /**
     * Set the value of constValue
     *
     * @param constValue new value of constValue
     */
    public void setConstValue(Serializable constValue) {
        this.constValue = constValue;
    }
}
