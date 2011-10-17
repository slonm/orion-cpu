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
    
    public void setAcademicStream(AcademicStream academicStream) {
        this.academicStream = academicStream;
    }
}
