/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.entities.uch;

import java.util.Date;
import javax.persistence.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.ref.*;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Сущность-учебный план подготовки по специальности (идентификатор и шапка)
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlan extends BaseEntity<EduPlan> {

    private LicenseRecord licenseRecord;
    //Термін навчання
    private Double trainingTerm;
    private Qualification qualification;
    private Date introducingDate;

    /**
     * @return запись лиценизии, привязанная к данному учебному плану
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public LicenseRecord getLicenseRecord() {
        try {
            return licenseRecord;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Устанавливает привязку данного учебного плана к лицензионной записи, определяющей:
     * образовательно-квалификационный уровень, код и название области знаний/направления
     * подготовки, код и название направления подготовки/специальности
     * @param licenseRecord
     */
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
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
        try {
            return licenseRecord.getKnowledgeAreaOrTrainingDirection();
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
     * @return срок обучения
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
     * @return название квалификации, получаемой в результате обучения
     * по данному учебному плану
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public Qualification getQualification() {
        return qualification;
    }

    /**
     * Привязывает название квалификации к данному учебному плану
     * @param qualification
     */
    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

     @Temporal(javax.persistence.TemporalType.DATE)
    public Date getIntroducingDate() {
        return introducingDate;
    }

    public void setIntroducingDate(Date introducingDate) {
        this.introducingDate = introducingDate;
    }

    @Override
    public String toString() {
        return (getCode()+ " " + getIntroducingDate());
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
