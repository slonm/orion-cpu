package orion.cpu.entities.sys;

import java.io.Serializable;
import javax.persistence.*;
import orion.cpu.baseentities.NamedEntity;

/**
 * Хранимые в базе данных константы в формате пар (ключ, значение)
 * Ключ - String
 * Значение - Serializable
 * @author sl
 */
@Entity
@Table(schema = "sys", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
public class StoredConstant extends NamedEntity<StoredConstant> {

    private Serializable constValue;

    /**
     * Get the value of constValue
     *
     * @return the value of constValue
     */
    @Column(length = 10000)
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
