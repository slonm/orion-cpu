package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Академическая группа. Используется для кадрового (деканатского) учета.
 * @author slobodyanuk
 */
@Entity
@Table(schema = "stu")
public class AcademicGroup extends StudentGroup<AcademicGroup> {

    @NotNull
    @ManyToOne
    private AcademicStream academicStream;

    public AcademicStream getAcademicStream() {
        return academicStream;
    }
    
    public AcademicGroup() {
    }

    /**
     * Создание академ группы
     * @param name - название (унаследовано то родителя - StudentGroup)
     * @param academicStream - учебный поток
     */
    public AcademicGroup(String name, AcademicStream academicStream) {
        setName(name);
        this.academicStream = academicStream;
    }

    public void setAcademicStream(AcademicStream academicStream) {
        this.academicStream = academicStream;
    }
}
