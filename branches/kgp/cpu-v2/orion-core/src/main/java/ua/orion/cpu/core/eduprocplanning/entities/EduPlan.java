package ua.orion.cpu.core.eduprocplanning.entities;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.utils.Defense;
import ua.orion.cpu.core.licensing.entities.EducationalQualificationLevel;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.licensing.entities.TrainingDirectionOrSpeciality;

/**
 * Сущность-учебный план подготовки по специальности (шапка)
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlan extends AbstractEntity<EduPlan> {

    private static final long serialVersionUID = 1L;
    
    private LicenseRecord licenseRecord;
    private Double trainingTerm;
    private Qualification qualification;
    private Calendar introducingDate;
    //Пользовательский тип данных для вывода списка циклов учебного плана в гриде
    @DataType("EduPlanDisciplineCycles")
    private Set<EduPlanDisciplineCycle> eduPlanDisciplineCycles = new HashSet<EduPlanDisciplineCycle>();

    public EduPlan() {
    }

    public EduPlan(LicenseRecord licenseRecord, Double trainingTerm, Qualification qualification,
            Calendar introducingDate, Set<EduPlanDisciplineCycle> eduPlanDisciplineCycles) {
        this.licenseRecord = licenseRecord;
        this.trainingTerm = trainingTerm;
        this.qualification = qualification;
        this.introducingDate = introducingDate;
        this.eduPlanDisciplineCycles = eduPlanDisciplineCycles;
    }

    /**
     * Однонаправленная ассоциация с лицензионной записью, определяющей:
     * образовательно-квалификационный уровень, код и название области знаний/направления
     * подготовки, код и название направления подготовки/специальности
     * @return запись лиценизии, привязанная к данному учебному плану
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public LicenseRecord getLicenseRecord() {
        try {
            return licenseRecord;
        } catch (NullPointerException e) {
            return null;
        }
    }

      public void setLicenseRecord(LicenseRecord licenseRecord) {
        this.licenseRecord = Defense.notNull(licenseRecord, "licenseRecord");
    }

    /**
     *  @return 4-значный код облаcти знаний/направления подготовки, привязанного
     * к данному плану (нужен для шапки плана)
     */
    @Transient
    public String getKATDCode() {
        try {
            return licenseRecord.getKnowledgeAreaOrTrainingDirectionCode();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название области знаний/направления подготовки, привязанного
     * к направлению обучения/специальности данного учебного плана
     * (нужен для шапки плана)
     */
    @Transient
    public String getKnowledgeAreaOrTrainingDirection() {
        try {
            return licenseRecord.getKnowledgeAreaOrTrainingDirectionName();
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
     * @return название направления подгoтовки/специальности данного учебного плана
     * (нужен для шапки плана)
     */
    @Transient
    public TrainingDirectionOrSpeciality getTrainingDirectionOrSpeciality() {
        try {
            return licenseRecord.getTrainingDirectionOrSpeciality();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return образовательно-квалифиувционный уровень направления подготовки/специальности,
     * привязанному к данному учебному плану (нужен для шапки плана)
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
     * @param trainingTerm the trainingTerm to set
     */
    public void setTrainingTerm(Double trainingTerm) {
        this.trainingTerm = trainingTerm;
    }

    /**
     * Однонаправленная ассоциация с квалификацией
     * @return название квалификации, получаемой в результате обучения
     * по данному учебному плану
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
    
    //TODO Написать компаратор, выводящий циклы в порядке возрастания их номера в учебном плане (eduPlanDisciplineCycleNumber)
    /**
     * Однонаправленная ассоциация с EduPlanDisciplineCycle
     * @return набор циклов дисциплин данного учебного плана
     */
//    @Sort(type = SortType.COMPARATOR, comparator=EduPlanDisciplineCycle.EduPlanDisciplineCycleComparator.class)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(schema="uch", joinColumns = { @JoinColumn(name = "EDUPLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "EDUPLANDISCIPLINECYCLE_ID") })
//    @JoinColumn(name = "EDUPLAN_ID")
    public Set<EduPlanDisciplineCycle> getEduPlanDisciplineCycles() {
        return eduPlanDisciplineCycles;
    }

    public void setEduPlanDisciplineCycles(Set<EduPlanDisciplineCycle> eduPlanDisciplineCycles) {
        this.eduPlanDisciplineCycles = eduPlanDisciplineCycles;
    }

    @Override
    public String toString() {
        return (getCode() + " " + getIntroducingDate());
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
