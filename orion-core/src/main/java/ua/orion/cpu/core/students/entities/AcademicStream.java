package ua.orion.cpu.core.students.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.licensing.entities.EducationForm;
import ua.orion.cpu.core.licensing.entities.TrainingDirectionOrSpeciality;

/**
 * Учебный поток (место поступления).
 *
 * @author sl
 */
@Entity
public class AcademicStream extends AbstractEntity<AcademicStream> {

    private static final long serialVersionUID = 1L;
    @NotNull
    @ManyToOne
    private TrainingDirectionOrSpeciality trainingDirectionOrSpeciality;
    @NotNull
    @ManyToOne
    private EducationForm educationForm;
    private Integer cource;

    /**
     * Создание учебного потока
     *
     * @param trainingDirectionOrSpeciality - Направление или специальность
     * @param educationForm - Форма обучения
     * @param cource - Курс
     */
    public AcademicStream(TrainingDirectionOrSpeciality trainingDirectionOrSpeciality, EducationForm educationForm, Integer cource) {
        this.trainingDirectionOrSpeciality = trainingDirectionOrSpeciality;
        this.educationForm = educationForm;
        this.cource = cource;
    }

    public AcademicStream() {
    }

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

    public TrainingDirectionOrSpeciality getTrainingDirectionOrSpeciality() {
        return trainingDirectionOrSpeciality;
    }

    public void setTrainingDirectionOrSpeciality(TrainingDirectionOrSpeciality trainingDirectionOrSpeciality) {
        this.trainingDirectionOrSpeciality = trainingDirectionOrSpeciality;
    }

    @Override
    public String toString() {
        return String.valueOf(trainingDirectionOrSpeciality) + " " + String.valueOf(educationForm) + " " + String.valueOf(cource);
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
