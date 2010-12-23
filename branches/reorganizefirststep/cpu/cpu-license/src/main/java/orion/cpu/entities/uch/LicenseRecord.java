package orion.cpu.entities.uch;

import java.util.Date;
import javax.persistence.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.OrgUnit;
import orion.cpu.entities.ref.EducationForm;
import orion.cpu.entities.ref.EducationalQualificationLevel;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.LicenseRecordGroup;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;
import ua.mihailslobodyanuk.utils.Defense;

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
    private LicenseRecordGroup licenseRecordGroup;

   @Transient
    public String getLicenseSerialNumber() {
       try {
            return (license.getSerial()+"  "+license.getNumber());
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Transient
    public Date getLicenseIssueDate() {
        try {
            return license.getIssue();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Transient
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
         try {
            return trainingDirectionOrSpeciality.getKnowledgeAreaOrTrainingDirection();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Transient
    public String getKnowledgeAreaOrTrainingDirectionCode() {
         try {
            return (getKnowledgeAreaOrTrainingDirection().getCode());
        } catch (NullPointerException e) {
            return null;
        }
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
        this.educationalQualificationLevel = Defense.notNull(educationalQualificationLevel, "educationalQualificationLevel");
    }

    @Transient
    public String getCode() {
        try {
            if (getEducationalQualificationLevel().getCode() == null ||
                    getKnowledgeAreaOrTrainingDirection().getCode() == null ||
                    getTrainingDirectionOrSpeciality().getCode() == null) {
                return null;
            }
            return (getEducationalQualificationLevel().getCode() + "." + getKnowledgeAreaOrTrainingDirection().getCode() + getTrainingDirectionOrSpeciality().getCode());
        } catch (NullPointerException e) {
            return null;
        }
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
        this.trainingDirectionOrSpeciality = Defense.notNull(TrainingDirectionOrSpeciality, "TrainingDirectionOrSpeciality");
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
        this.educationForm = Defense.notNull(EducationForm, "EducationForm");
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
        this.terminationDate = Defense.notNull(terminationDate, "terminationDate");
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
        this.orgUnit = Defense.notNull(orgUnit, "orgUnit");
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
        this.license = Defense.notNull(license, "license");
    }

    @JoinColumn(nullable = false)
    @ManyToOne
    public LicenseRecordGroup getLicenseRecordGroup() {
        return licenseRecordGroup;
    }

    public void setLicenseRecordGroup(LicenseRecordGroup licenseRecordGroup) {
        this.licenseRecordGroup = Defense.notNull(licenseRecordGroup, "licenseRecordGroup");
    }

    @Override
    public String toString() {
        return getCode()+" - "+educationForm;
    }

    @Override
    protected boolean entityEquals(LicenseRecord obj) {
        return aEqualsField(trainingDirectionOrSpeciality, obj.trainingDirectionOrSpeciality)
                && aEqualsField(educationalQualificationLevel, obj.educationalQualificationLevel);
    }

    @Override
    public int compareTo(LicenseRecord o) {
        return o.toString().compareTo(o.toString());
    }

    
}
