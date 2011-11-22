package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.licensing.entities.EducationForm;
import ua.orion.cpu.core.licensing.entities.TrainingDirection;

/**
 * Учебный поток (место поступления).
 * @author sl
 */
@Entity
@Table(schema = "uch")
public class AcademicStream extends AbstractEntity<AcademicStream> {

    private static final long serialVersionUID = 1L;
    @NotNull
    @ManyToOne
    private TrainingDirection trainingDirectionOrSpeciality;
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

    public TrainingDirection getTrainingDirectionOrSpeciality() {
        return trainingDirectionOrSpeciality;
    }

    public void setTrainingDirectionOrSpeciality(TrainingDirection trainingDirectionOrSpeciality) {
        this.trainingDirectionOrSpeciality = trainingDirectionOrSpeciality;
    }

    @Override
    public String toString() {
        return "AcademicStream{" + "trainingDirectionOrSpeciality="
                + String.valueOf(trainingDirectionOrSpeciality)
                + ", educationForm=" + String.valueOf(educationForm) + ", cource="
                + String.valueOf(cource) + '}';
    }

    //Недостаточно информации для проверки эквивалентности
    @Override
    protected boolean entityEquals(AcademicStream obj) {
        return aEqualsField(trainingDirectionOrSpeciality, obj.trainingDirectionOrSpeciality)
                && aEqualsField(educationForm, obj.educationForm)
                && aEqualsField(cource, obj.cource);
    }

    @Override
    public int compareTo(AcademicStream o) {
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }
}
