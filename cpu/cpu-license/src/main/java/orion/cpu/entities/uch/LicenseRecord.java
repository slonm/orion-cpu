package orion.cpu.entities.uch;

import java.util.Date;
import javax.persistence.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.OrgUnit;
import orion.cpu.entities.ref.EducationForm;
import orion.cpu.entities.ref.EducationalQualificationLevel;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;

/**
 * Сущность подситемы учета лицензий
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class LicenseRecord extends BaseEntity<LicenseRecord> {

    private EducationalQualificationLevel educationalQualificationLevel;
    private TrainingDirectionOrSpeciality trainingDirectionOrSpeciality;
    private EducationForm educationForm;
    private Integer studentLicenseQuantity;
    private Date terminationDate;
    private OrgUnit orgUnit;
    private License license;


    
    @Transient
    public String getLicenseSerialNumber() {
        return (license.getSerial()+"  "+license.getNumber());
    }

    @Transient
    public Date getLicenseIssueDate() {
        return license.getIssue();
    }

    @Transient
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
        return trainingDirectionOrSpeciality.getKnowledgeAreaOrTrainingDirection();
    }

    @Transient
    public String getKnowledgeAreaOrTrainingDirectionCode() {
        return trainingDirectionOrSpeciality.getKnowledgeAreaOrTrainingDirection().getCode();
    }

    /**
     * @return the educationalQualificationLevel
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public EducationalQualificationLevel getEducationalQualificationLevel() {
        return educationalQualificationLevel;
    }

    /**
     * @param educationalQualificationLevel the educationalQualificationLevel to set
     */
    public void setEducationalQualificationLevel(EducationalQualificationLevel educationalQualificationLevel) {
        this.educationalQualificationLevel = educationalQualificationLevel;
    }

    @Transient
    public String getCode() {
        return (educationalQualificationLevel.getCode() + "." + trainingDirectionOrSpeciality.getKnowledgeAreaOrTrainingDirection().getCode() + trainingDirectionOrSpeciality.getCode());
    }

    /**
     * @return the TrainingDirectionOrSpeciality
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public TrainingDirectionOrSpeciality getTrainingDirectionOrSpeciality() {
        return trainingDirectionOrSpeciality;
    }

    /**
     * @param TrainingDirectionOrSpeciality the TrainingDirectionOrSpeciality to set
     */
    public void setTrainingDirectionOrSpeciality(TrainingDirectionOrSpeciality TrainingDirectionOrSpeciality) {
        this.trainingDirectionOrSpeciality = TrainingDirectionOrSpeciality;
    }

    /**
     * @return the EducationForm
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public EducationForm getEducationForm() {
        return educationForm;
    }

    /**
     * @param EducationForm the EducationForm to set
     */
    public void setEducationForm(EducationForm EducationForm) {
        this.educationForm = EducationForm;
    }

    /**
     * @return the studentLicenseQuantity
     */
    public Integer getStudentLicenseQuantity() {
        return studentLicenseQuantity;
    }

    /**
     * @param studentLicenseQuantity the studentLicenseQuantity to set
     */
    public void setStudentLicenseQuantity(Integer studentLicenseQuantity) {
        this.studentLicenseQuantity = studentLicenseQuantity;
    }

    /**
     * @return the terminationDate
     */
    @Temporal(value = javax.persistence.TemporalType.DATE)
    public Date getTerminationDate() {
        return terminationDate;
    }

    /**
     * @param terminationDate the terminationDate to set
     */
    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    /**
     * @return the orgUnit
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    /**
     * @return the license
     */
//    @NonVisual
    @JoinColumn(nullable = false)
    @ManyToOne
    public License getLicense() {
        return license;
    }

    /**
     * @param license the license to set
     */
    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return getCode();
    }

    @Override
    protected boolean entityEquals(LicenseRecord obj) {
        return aEqualsField(trainingDirectionOrSpeciality, obj.trainingDirectionOrSpeciality)
                && aEqualsField(orgUnit, obj.orgUnit);
    }

    @Override
    public int compareTo(LicenseRecord o) {
        return o.toString().compareTo(o.toString());
    }

    
}