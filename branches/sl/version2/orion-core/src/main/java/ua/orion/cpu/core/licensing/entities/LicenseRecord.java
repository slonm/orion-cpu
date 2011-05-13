package ua.orion.cpu.core.licensing.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.hibernate.annotations.Formula;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.utils.Defense;

/**
 * Сущность подситемы учета лицензий
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class LicenseRecord extends AbstractEntity<LicenseRecord> {

    private EducationalQualificationLevel educationalQualificationLevel;
    private TrainingDirectionOrSpeciality trainingDirectionOrSpeciality;
    private Calendar termination;
    private OrgUnit orgUnit;
    private License license;
    private LicenseRecordGroup licenseRecordGroup;
    private Map<EducationForm, Integer> licenseQuantityByEducationForm = new HashMap();
    private String knowledgeAreaOrTrainingDirectionCode;
    private String knowledgeAreaOrTrainingDirectionName;

    public LicenseRecord() {
    }

    public LicenseRecord(License license,
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality,
            EducationalQualificationLevel educationalQualificationLevel,
            Map<EducationForm, Integer> licenseQuantityByEducationForm,
            Calendar termination,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {
        this.license = license;
        this.trainingDirectionOrSpeciality = trainingDirectionOrSpeciality;
        this.educationalQualificationLevel = educationalQualificationLevel;
        this.licenseQuantityByEducationForm = licenseQuantityByEducationForm;
        this.termination = termination;
        this.orgUnit = orgUnit;
        this.licenseRecordGroup = licenseRecordGroup;
    }

    @Formula("(select ka.name from ref.Training_Direction_Or_Speciality tr join ref.knowledge_Area_Or_Training_Direction ka on tr.knowledge_Area_Or_Training_Direction=ka.id where tr.id=training_Direction_Or_Speciality)")
    public String getKnowledgeAreaOrTrainingDirectionName() {
        return knowledgeAreaOrTrainingDirectionName;
    }

    public void setKnowledgeAreaOrTrainingDirectionName(String knowledgeAreaOrTrainingDirectionName) {
        this.knowledgeAreaOrTrainingDirectionName = knowledgeAreaOrTrainingDirectionName;
    }

    @Transient
    @NonVisual
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
        try {
            return trainingDirectionOrSpeciality.getKnowledgeAreaOrTrainingDirection();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @ElementCollection
    @CollectionTable(schema = "uch")
    public Map<EducationForm, Integer> getLicenseQuantityByEducationForm() {
        return licenseQuantityByEducationForm;
    }

    public void setLicenseQuantityByEducationForm(Map<EducationForm, Integer> licenseQuantityByEducationForm) {
        this.licenseQuantityByEducationForm = licenseQuantityByEducationForm;
    }

    @Formula("(select ka.code from ref.Training_Direction_Or_Speciality tr join ref.knowledge_Area_Or_Training_Direction ka on tr.knowledge_Area_Or_Training_Direction=ka.id where tr.id=training_Direction_Or_Speciality)")
    public String getKnowledgeAreaOrTrainingDirectionCode() {
        return knowledgeAreaOrTrainingDirectionCode;
    }

    public void setKnowledgeAreaOrTrainingDirectionCode(String knowledgeAreaOrTrainingDirectionCode) {
        this.knowledgeAreaOrTrainingDirectionCode = knowledgeAreaOrTrainingDirectionCode;
    }

    /**
     * @return the educationalQualificationLevel
     */
    @ManyToOne
    @NotNull
    public EducationalQualificationLevel getEducationalQualificationLevel() {
        return educationalQualificationLevel;
    }

    /**
     * @param educationalQualificationLevel the educationalQualificationLevel to set
     */
    public void setEducationalQualificationLevel(EducationalQualificationLevel educationalQualificationLevel) {
        this.educationalQualificationLevel = Defense.notNull(educationalQualificationLevel, "educationalQualificationLevel");
    }

    @Transient
    public String getCode() {
        try {
            StringBuilder sb = new StringBuilder(getEducationalQualificationLevel().getCode()).append(".").append(getKnowledgeAreaOrTrainingDirection().getCode()).append(getTrainingDirectionOrSpeciality().getCode());
            return sb.toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return the TrainingDirectionOrSpeciality
     */
    @ManyToOne
    @NotNull
    public TrainingDirectionOrSpeciality getTrainingDirectionOrSpeciality() {
        return trainingDirectionOrSpeciality;
    }

    /**
     * @param TrainingDirectionOrSpeciality the TrainingDirectionOrSpeciality to set
     */
    public void setTrainingDirectionOrSpeciality(TrainingDirectionOrSpeciality TrainingDirectionOrSpeciality) {
        this.trainingDirectionOrSpeciality = Defense.notNull(TrainingDirectionOrSpeciality, "TrainingDirectionOrSpeciality");
    }

    /**
     * @return the termination
     */
    @Temporal(value = javax.persistence.TemporalType.DATE)
    @NotNull
    public Calendar getTermination() {
        return termination;
    }

    /**
     * @param termination the terminationDate to set
     */
    public void setTermination(Calendar termination) {
        this.termination = Defense.notNull(termination, "termination");
    }

    /**
     * @return the orgUnit
     */
    @ManyToOne
    @NotNull
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = Defense.notNull(orgUnit, "orgUnit");
    }

    /**
     * @return the license
     */
    @ManyToOne
    @NotNull
    public License getLicense() {
        return license;
    }

    /**
     * @param license the license to set
     */
    public void setLicense(License license) {
        this.license = Defense.notNull(license, "license");
    }

    @ManyToOne
    @NotNull
    public LicenseRecordGroup getLicenseRecordGroup() {
        return licenseRecordGroup;
    }

    public void setLicenseRecordGroup(LicenseRecordGroup licenseRecordGroup) {
        this.licenseRecordGroup = Defense.notNull(licenseRecordGroup, "licenseRecordGroup");
    }

    @Override
    public String toString() {
        return getCode();
    }

    @Override
    protected boolean entityEquals(LicenseRecord obj) {
        return aEqualsField(trainingDirectionOrSpeciality, obj.trainingDirectionOrSpeciality)
                && aEqualsField(educationalQualificationLevel, obj.educationalQualificationLevel);
    }

    @Override
    public int compareTo(LicenseRecord o) {
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }
}
