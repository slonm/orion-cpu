package orion.cpu.entities.uch;

import java.util.Date;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.OrgUnit;
import orion.cpu.entities.ref.EducationalQualificationLevel;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.LicenseRecordGroup;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Сущность подситемы учета лицензий
 * @author kgp
 */
public class LicenseRecordView extends BaseEntity<LicenseRecordView> {

    private LicenseRecord stationaryLicenseRecord = new LicenseRecord();
    private LicenseRecord correspondenseLicenseRecord = new LicenseRecord();

    public LicenseRecordView() {
    }

    @Override
    public Integer getId() {
        return stationaryLicenseRecord.getId();
    }

    @NonVisual
    public LicenseRecord getCorrespondenseLicenseRecord() {
        return correspondenseLicenseRecord;
    }

    public void setCorrespondenseLicenseRecord(LicenseRecord correspondenseLicenseRecord) {
        this.correspondenseLicenseRecord = Defense.notNull(correspondenseLicenseRecord, "correspondenseLicenseRecord");
    }

    @NonVisual
    public LicenseRecord getStationaryLicenseRecord() {
        return stationaryLicenseRecord;
    }

    public void setStationaryLicenseRecord(LicenseRecord stationaryLicenseRecord) {
        this.stationaryLicenseRecord = Defense.notNull(stationaryLicenseRecord, "stationaryLicenseRecord");
    }

    public LicenseRecordView(LicenseRecord stationaryLicenseRecord, LicenseRecord correspondenseLicenseRecord) {
        this.stationaryLicenseRecord = Defense.notNull(stationaryLicenseRecord, "stationaryLicenseRecord");
        this.correspondenseLicenseRecord = Defense.notNull(correspondenseLicenseRecord, "correspondenseLicenseRecord");
    }

    public String getLicenseSerialNumber() {
        try {
            return (stationaryLicenseRecord.getLicense().getSerial() + "  "
                    + stationaryLicenseRecord.getLicense().getNumber());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Date getLicenseIssueDate() {
        try {
            return stationaryLicenseRecord.getLicense().getIssue();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getKnowledgeAreaOrTrainingDirectionCode() {
        try {
            return stationaryLicenseRecord.getKnowledgeAreaOrTrainingDirection().getCode();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
        return stationaryLicenseRecord.getKnowledgeAreaOrTrainingDirection();
    }

    /**
     * @return the educationalQualificationLevel
     */
//    @NonVisual
    public EducationalQualificationLevel getEducationalQualificationLevel() {
        return stationaryLicenseRecord.getEducationalQualificationLevel();
    }

    /**
     * @param educationalQualificationLevel the educationalQualificationLevel to set
     */
    @Validate("required")
    public void setEducationalQualificationLevel(EducationalQualificationLevel educationalQualificationLevel) {
        stationaryLicenseRecord.setEducationalQualificationLevel(Defense.notNull(educationalQualificationLevel, "educationalQualificationLevel"));
        correspondenseLicenseRecord.setEducationalQualificationLevel(Defense.notNull(educationalQualificationLevel, "educationalQualificationLevel"));
    }

    public String getCode() {
        return stationaryLicenseRecord.getCode();
    }

    /**
     * @return the TrainingDirectionOrSpeciality
     */
    public TrainingDirectionOrSpeciality getTrainingDirectionOrSpeciality() {
        return stationaryLicenseRecord.getTrainingDirectionOrSpeciality();
    }

    /**
     * @param trainingDirectionOrSpeciality the TrainingDirectionOrSpeciality to set
     */
    @Validate("required")
    public void setTrainingDirectionOrSpeciality(TrainingDirectionOrSpeciality trainingDirectionOrSpeciality) {
        stationaryLicenseRecord.setTrainingDirectionOrSpeciality(Defense.notNull(trainingDirectionOrSpeciality, "trainingDirectionOrSpeciality"));
        correspondenseLicenseRecord.setTrainingDirectionOrSpeciality(Defense.notNull(trainingDirectionOrSpeciality, "trainingDirectionOrSpeciality"));
    }

    /**
     * @return the stationaryStudentLicenseQuantity
     */
    public Integer getStationaryStudentLicenseQuantity() {
        return stationaryLicenseRecord.getStudentLicenseQuantity();
    }

    /**
     * @param stationaryStudentLicenseQuantity the stationaryStudentLicenseQuantity to set
     */
    @Validate("required")
    public void setStationaryStudentLicenseQuantity(Integer stationaryStudentLicenseQuantity) {
        stationaryLicenseRecord.setStudentLicenseQuantity(stationaryStudentLicenseQuantity);
    }

    /**
     * @return the correspondenseStudentLicenseQuantity
     */
    public Integer getCorrespondenseStudentLicenseQuantity() {
        return correspondenseLicenseRecord.getStudentLicenseQuantity();
    }

    /**
     * @param correspondenseStudentLicenseQuantity the correspondenseStudentLicenseQuantity to set
     */
    @Validate("required")
    public void setCorrespondenseStudentLicenseQuantity(Integer correspondenseStudentLicenseQuantity) {
        correspondenseLicenseRecord.setStudentLicenseQuantity(correspondenseStudentLicenseQuantity);
    }

    /**
     * @return the terminationDate
     */
    public Date getTerminationDate() {
        return stationaryLicenseRecord.getTerminationDate();
    }

    /**
     * @param terminationDate the terminationDate to set
     */
    @Validate("required")
    public void setTerminationDate(Date terminationDate) {
        stationaryLicenseRecord.setTerminationDate(Defense.notNull(terminationDate, "terminationDate"));
        correspondenseLicenseRecord.setTerminationDate(Defense.notNull(terminationDate, "terminationDate"));
    }

    /**
     * @return the Unit
     */
    public OrgUnit getOrgUnit() {
        return stationaryLicenseRecord.getOrgUnit();
    }

    /**
     * @param orgUnit to set
     */
    @Validate("required")
    public void setOrgUnit(OrgUnit orgUnit) {
        stationaryLicenseRecord.setOrgUnit(Defense.notNull(orgUnit, "orgUnit"));
        correspondenseLicenseRecord.setOrgUnit(Defense.notNull(orgUnit, "orgUnit"));
    }

    /**
     * @return the license
     */
//    @NonVisual
    public License getLicense() {
        return stationaryLicenseRecord.getLicense();
    }

    /**
     * @param license the license to set
     */
    @Validate("required")
    public void setLicense(License license) {
        stationaryLicenseRecord.setLicense(Defense.notNull(license, "license"));
        correspondenseLicenseRecord.setLicense(Defense.notNull(license, "license"));
    }

    @Validate("required")
    public LicenseRecordGroup getLicenseRecordGroup() {
        return stationaryLicenseRecord.getLicenseRecordGroup();
    }

    @Validate("required")
    public void setLicenseRecordGroup(LicenseRecordGroup licenseRecordGroup) {
        stationaryLicenseRecord.setLicenseRecordGroup(Defense.notNull(licenseRecordGroup, "licenseRecordGroup"));
        correspondenseLicenseRecord.setLicenseRecordGroup(Defense.notNull(licenseRecordGroup, "licenseRecordGroup"));
    }

    @Override
    public int compareTo(LicenseRecordView o) {
        int c = stationaryLicenseRecord.compareTo(o.stationaryLicenseRecord);
        if (c != 0) {
            return c;
        }
        c = correspondenseLicenseRecord.compareTo(o.stationaryLicenseRecord);
        if (c != 0) {
            return c;
        }
        return 0;
    }

    @Override
    public String toString() {
        return getCode();
    }

    @Override
    protected boolean entityEquals(LicenseRecordView obj) {
        return aEqualsField(stationaryLicenseRecord, obj.stationaryLicenseRecord) && aEqualsField(correspondenseLicenseRecord, obj.correspondenseLicenseRecord);
    }
}
