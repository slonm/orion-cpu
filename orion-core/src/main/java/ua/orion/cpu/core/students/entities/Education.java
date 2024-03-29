package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - образование Хранит виды образования - "Среднее
 * техническое, Специалист" и прочие
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class Education extends AbstractEnumerationEntity<Education> {

    private static final long serialVersionUID = 1L;

    public Education() {
    }

    /**
     * Создание экземпляра образования на основе имени.
     *
     * @param name - уникальное имя. Например ("Высшее образование")
     */
    public Education(String name) {
        setName(name);
    }
}
