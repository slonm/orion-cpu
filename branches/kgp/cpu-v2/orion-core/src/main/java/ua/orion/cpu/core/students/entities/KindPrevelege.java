package ua.orion.cpu.core.students.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Справочник - вид привелегии. Содержит уникальное имя. 
 *
 * @author molodec
 */
@Entity
@Table(schema = "ref")
public class KindPrevelege extends AbstractEnumerationEntity<KindPrevelege> {

    private static final long serialVersionUID = 1L;
    private String shortName;

    /**
     * Создание экземпляра Вид привелегии
     *
     * @param name - уникальное имя(название)
     * @param shortName - сокращенное название.
     */
    public KindPrevelege(String name, String shortName) {
        setName(name);
        this.shortName = shortName;
    }

    public KindPrevelege() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
