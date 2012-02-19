package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.licensing.entities.EducationForm;
import ua.orion.cpu.core.licensing.entities.Speciality;
import ua.orion.cpu.core.licensing.entities.TrainingDirection;

/**
 * Учебный поток (место поступления).
 * @author sl
 */
@Entity
@Table
public class AcademicStream extends AbstractEntity<AcademicStream> {

    private static final long serialVersionUID = 1L;
    @NotNull
    @ManyToOne
    private TrainingDirection trainingDirection;
    @NotNull
    @ManyToOne
    private Speciality speciality;
    @NotNull
    @ManyToOne
    private EducationForm educationForm;
    private Integer cource;

    public Integer getCource() {
        return cource;
    }

    public void setCource(Integer cource) {
        this.cource = cource;
    }

    public EducationForm getEducationForm() {
        return educationForm;
    }

    public void setEducationForm(EducationForm educationForm) {
        this.educationForm = educationForm;
    }

    public TrainingDirection getTrainingDirection() {
        return trainingDirection;
    }

    public void setTrainingDirection(TrainingDirection trainingDirection) {
        this.trainingDirection = trainingDirection;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "AcademicStream{" + "speciality="
                + String.valueOf(speciality)
                + " trainingDirection="
                + String.valueOf(trainingDirection)
                + " educationForm=" + String.valueOf(educationForm) + ", cource="
                + String.valueOf(cource) + '}';
    }

    //Недостаточно информации для проверки эквивалентности
    @Override
    protected boolean entityEquals(AcademicStream obj) {
        return aEqualsField(trainingDirection, obj.trainingDirection)
                && aEqualsField(speciality, obj.speciality)
                && aEqualsField(educationForm, obj.educationForm)
                && aEqualsField(cource, obj.cource);
    }

    @Override
    public int compareTo(AcademicStream o) {
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }
}
