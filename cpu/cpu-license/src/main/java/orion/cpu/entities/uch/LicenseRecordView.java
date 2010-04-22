package orion.cpu.entities.uch;

import java.util.Date;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.OrgUnit;
import orion.cpu.entities.ref.EducationalQualificationLevel;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;

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
    public Integer getId(){
        return stationaryLicenseRecord.getId();
    }

    @NonVisual
    public LicenseRecord getCorrespondenseLicenseRecord() {
        return correspondenseLicenseRecord;
    }

    public void setCorrespondenseLicenseRecord(LicenseRecord correspondenseLicenseRecord) {
        this.correspondenseLicenseRecord = correspondenseLicenseRecord;
    }

    @NonVisual
    public LicenseRecord getStationaryLicenseRecord() {
        return stationaryLicenseRecord;
    }

    public void setStationaryLicenseRecord(LicenseRecord stationaryLicenseRecord) {
        this.stationaryLicenseRecord = stationaryLicenseRecord;
    }

    public LicenseRecordView(LicenseRecord stationaryLicenseRecord, LicenseRecord correspondenseLicenseRecord) {
        this.stationaryLicenseRecord = stationaryLicenseRecord;
        this.correspondenseLicenseRecord = correspondenseLicenseRecord;
    }

    public String getLicenseSerialNumber() {
        return (stationaryLicenseRecord.getLicense().getSerial() + "  " +
                stationaryLicenseRecord.getLicense().getNumber());
    }

    public Date getLicenseIssueDate() {
        return stationaryLicenseRecord.getLicense().getIssue();
    }

    public String getKnowledgeAreaOrTrainingDirectionCode() {
        return stationaryLicenseRecord.getKnowledgeAreaOrTrainingDirection().getCode();
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
        stationaryLicenseRecord.setEducationalQualificationLevel(educationalQualificationLevel);
        correspondenseLicenseRecord.setEducationalQualificationLevel(educationalQualificationLevel);
    }

    public String getCode() {
        return (getEducationalQualificationLevel().getCode() + "." + getKnowledgeAreaOrTrainingDirection().getCode() + getTrainingDirectionOrSpeciality().getCode());
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
        stationaryLicenseRecord.setTrainingDirectionOrSpeciality(trainingDirectionOrSpeciality);
        correspondenseLicenseRecord.setTrainingDirectionOrSpeciality(trainingDirectionOrSpeciality);
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
        stationaryLicenseRecord.setTerminationDate(terminationDate);
        correspondenseLicenseRecord.setTerminationDate(terminationDate);
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
        stationaryLicenseRecord.setOrgUnit(orgUnit);
        correspondenseLicenseRecord.setOrgUnit(orgUnit);
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
        stationaryLicenseRecord.setLicense(license);
        correspondenseLicenseRecord.setLicense(license);
    }

    @Override
    public int compareTo(LicenseRecordView o) {
        int c = stationaryLicenseRecord.compareTo(o.stationaryLicenseRecord);
        if(c!=0) return c;
        c = correspondenseLicenseRecord.compareTo(o.stationaryLicenseRecord);
        if(c!=0) return c;
        return 0;
    }

    @Override
    public String toString() {
        return getCode();
    }

    @Override
    protected boolean entityEquals(LicenseRecordView obj) {
        return aEqualsField(stationaryLicenseRecord, obj.stationaryLicenseRecord)
                && aEqualsField(correspondenseLicenseRecord, obj.correspondenseLicenseRecord);
    }
}
