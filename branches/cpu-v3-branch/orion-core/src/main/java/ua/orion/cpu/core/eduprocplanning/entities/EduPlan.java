package ua.orion.cpu.core.eduprocplanning.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.cpu.core.licensing.entities.EducationalQualificationLevel;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.licensing.entities.TrainingDirection;

/**
 * Сущность-учебный план подготовки по специальности (шапка)
 *
 * @author kgp
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"qualification",
    "licenseRecord", "introducingDate"}))
public class EduPlan extends AbstractEntity<EduPlan> {

    private static final long serialVersionUID = 1L;
    private LicenseRecord licenseRecord;
    private Double trainingTerm;
    private Qualification qualification;
    private Calendar introducingDate;
    //Дисциплины учебного плана
    private Set<EduPlanDiscipline> eduPlanDisciplines = new HashSet<EduPlanDiscipline>();
    private Calendar confirmationDate;
    private String confirmationPerson;
    private Boolean isValid;

    public EduPlan() {
    }

    public EduPlan(LicenseRecord licenseRecord, Double trainingTerm, Qualification qualification,
            Calendar introducingDate, Calendar confirmationDate, String confirmationPerson, Boolean isValid) {
        this.licenseRecord = licenseRecord;
        this.trainingTerm = trainingTerm;
        this.qualification = qualification;
        this.introducingDate = introducingDate;
        this.confirmationDate = confirmationDate;
        this.confirmationPerson = confirmationPerson;
        this.isValid = isValid;
    }

    /**
     * Однонаправленная ассоциация с лицензионной записью, определяющей:
     * образовательно-квалификационный уровень, код и название области
     * знаний/направления подготовки, код и название направления
     * подготовки/специальности
     *
     * @return запись лиценизии, привязанная к данному учебному плану
     */
    @ManyToOne
    @JoinColumn(name = "licenseRecord", nullable = false)
    public LicenseRecord getLicenseRecord() {
        try {
            return licenseRecord;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setLicenseRecord(LicenseRecord licenseRecord) {
        this.licenseRecord = licenseRecord;
    }

    /**
     * @return 4-значный код облаcти знаний/направления подготовки, привязанного
     * к данному плану (нужен для шапки плана)
     */
    @Transient
    public String getKnowledgeAreaCode() {
        try {
            return licenseRecord.getKnowledgeAreaCode();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название области знаний/направления подготовки, привязанного к
     * направлению обучения/специальности данного учебного плана (нужен для
     * шапки плана)
     */
    @Transient
    public String getKnowledgeAreaName() {
        try {
            return licenseRecord.getKnowledgeAreaName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return составной код типа 6.080403 направления подгoтовки/специальности
     * данного учебного плана (нужен для шапки плана)
     */
    @Transient
    public String getCode() {
        try {
            return licenseRecord.getCode();
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * @return название направления подгoтовки данного учебного плана (нужен для
     * шапки плана)
     */
    @Transient
    public String getTrainingDirectionName() {
        try {
            return licenseRecord.getTrainingDirection().getName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название специальности данного учебного плана (нужен для шапки
     * плана)
     */
    @Transient
    public String getSpecialityName() {
        try {
            return licenseRecord.getSpeciality().getName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название групп лицензионных записей (подготовка бакалавров, для
     * колледжа и т.д.)
     */
    @Transient
    public String getLicenseRecordGroupName() {
        try {
            return licenseRecord.getLicenseRecordGroup().getName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return образовательно-квалифиувционный уровень направления
     * подготовки/специальности, привязанному к данному учебному плану (нужен
     * для шапки плана)
     */
    @Transient
    public EducationalQualificationLevel getEducationalQualificationLevel() {
        try {
            return licenseRecord.getEducationalQualificationLevel();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return срок обучения (в годах) под данному учебному плану
     */
    public Double getTrainingTerm() {
        return trainingTerm;
    }

    /**
     * Устанавливает значение свойства плана Срок обучения
     *
     * @param trainingTerm the trainingTerm to set
     */
    public void setTrainingTerm(Double trainingTerm) {
        this.trainingTerm = trainingTerm;
    }

    /**
     * Однонаправленная ассоциация с квалификацией
     *
     * @return название квалификации, получаемой в результате обучения по
     * данному учебному плану
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getIntroducingDate() {
        return introducingDate;
    }

    public void setIntroducingDate(Calendar introducingDate) {
        this.introducingDate = introducingDate;
    }

    //Двунаправленная ассоциация с дисциплинами учебного плана, входящими в данный цикл
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eduPlan")
    public Set<EduPlanDiscipline> getEduPlanDisciplines() {
        return eduPlanDisciplines;
    }

    public void setEduPlanDisciplines(Set<EduPlanDiscipline> eduPlanDisciplines) {
        this.eduPlanDisciplines = eduPlanDisciplines;
    }

    /**
     * @return Дата затвердження
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Calendar confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    /**
     * @return Кто затвердив
     */
    @DataType("longtext")
    public String getConfirmationPerson() {
        return confirmationPerson;
    }

    public void setConfirmationPerson(String confirmationPerson) {
        this.confirmationPerson = confirmationPerson;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return (getCode() + " - " + formatter.format(getIntroducingDate().getTime()));
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected boolean entityEquals(EduPlan obj) {
        return aEqualsField(licenseRecord, obj.licenseRecord) && aEqualsField(introducingDate, obj.introducingDate);
    }

    @Override
    public int compareTo(EduPlan o) {
        return o.toString().compareTo(o.toString());
    }
}
