package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочник - вид платильщика. Содержит уникальное имя. Например, юридическое
 * или физическое лицо
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class KindPayer extends AbstractEnumerationEntity<KindPayer> {

    private static final long serialVersionUID = 1L;
    private String shortName;

    /**
     * Создание экземпляра Вид платильщика
     *
     * @param name - уникальное имя(название)
     * @param shortName - сокращенное название.
     */
    public KindPayer(String name, String shortName) {
        setName(name);
        this.shortName = shortName;
    }

    public KindPayer() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
