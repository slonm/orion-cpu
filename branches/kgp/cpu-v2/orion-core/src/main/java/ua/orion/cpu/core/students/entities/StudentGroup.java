package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Группа студентов произвольного назначения
 * @author sl
 */
@Entity
@Table(schema = "stu")
@Inheritance(strategy = InheritanceType.JOINED)
public class StudentGroup<T extends AbstractEnumerationEntity<?>> extends AbstractEnumerationEntity<T> {

    private static final long serialVersionUID = 1L;
}
