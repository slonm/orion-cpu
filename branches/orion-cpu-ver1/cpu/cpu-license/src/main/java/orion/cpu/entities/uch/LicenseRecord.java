package orion.cpu.entities.uch;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.apache.tapestry5.beaneditor.DataType;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.OrgUnit;
import orion.cpu.entities.ref.EducationForm;
import orion.cpu.entities.ref.EducationalQualificationLevel;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.LicenseRecordGroup;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Сущность подсистемы учета лицензий
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class LicenseRecord extends BaseEntity<LicenseRecord> {

    private License license;
    private TrainingDirectionOrSpeciality trainingDirectionOrSpeciality;
    private EducationalQualificationLevel educationalQualificationLevel;
    //Создание пользовательского типа данных, указывающего на Property Block,
    //используемый в гриде и бинэдиторе
    @DataType("EduFormLicenseQuantity")
    private SortedMap<EducationForm, Integer> licenseQuantityByEducationForm=new TreeMap();
    private Date terminationDate;
    private OrgUnit orgUnit;
    private LicenseRecordGroup licenseRecordGroup;

    public LicenseRecord() {
    }

    public LicenseRecord(License license,
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality,
            EducationalQualificationLevel educationalQualificationLevel,
            SortedMap<EducationForm, Integer> licenseQuantityByEducationForm,
            Date terminationDate,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {
                this.license=license;
                this.trainingDirectionOrSpeciality=trainingDirectionOrSpeciality;
                this.educationalQualificationLevel=educationalQualificationLevel;
                this.licenseQuantityByEducationForm=licenseQuantityByEducationForm;
                this.terminationDate=terminationDate;
                this.orgUnit=orgUnit;
                this.licenseRecordGroup=licenseRecordGroup;
    }

    /**
     * @return Серия, номер и дата выдачи лицензии, к которой принадлежит данная запись
     * (не отображается в гриде)
     */

    @ManyToOne
    @NotNull
    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = Defense.notNull(license, "license");
    }

    /**
     * @return код области знаний/направления обучения
     */
    @Transient
    public String getKnowledgeAreaOrTrainingDirectionCode() {
         try {
            return (getKnowledgeAreaOrTrainingDirection().getCode());
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название области знаний/направления обучения
     */
     @Transient
    public KnowledgeAreaOrTrainingDirection getKnowledgeAreaOrTrainingDirection() {
         try {
            return trainingDirectionOrSpeciality.getKnowledgeAreaOrTrainingDirection();
        } catch (NullPointerException e) {
            return null;
        }
    }

     /**
     * @return образовательно-квалификайционный уровень
     * (не отображается в гриде)
     */

    @ManyToOne
    @NotNull
    public EducationalQualificationLevel getEducationalQualificationLevel() {
        return educationalQualificationLevel;
    }

    public void setEducationalQualificationLevel(EducationalQualificationLevel educationalQualificationLevel) {
        this.educationalQualificationLevel = Defense.notNull(educationalQualificationLevel, "educationalQualificationLevel");
    }

    /**
     * @return составной код, включающий код образовательно-квалификационного уровня,
     * код области знаний/направления обучения и код направления обучения/специальности
     */
    @Transient
    public String getCode() {
        try {
            if (getEducationalQualificationLevel().getCode() == null ||
                    getKnowledgeAreaOrTrainingDirection().getCode() == null ||
                    getTrainingDirectionOrSpeciality().getCode() == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder(getEducationalQualificationLevel().getCode())
                    .append(".").append(getKnowledgeAreaOrTrainingDirection().getCode())
                    .append(getTrainingDirectionOrSpeciality().getCode());
            return sb.toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название направления обучения/специальности
     */
    @ManyToOne
    @NotNull
    public TrainingDirectionOrSpeciality getTrainingDirectionOrSpeciality() {
        return trainingDirectionOrSpeciality;
    }

    public void setTrainingDirectionOrSpeciality(TrainingDirectionOrSpeciality TrainingDirectionOrSpeciality) {
        this.trainingDirectionOrSpeciality = Defense.notNull(TrainingDirectionOrSpeciality, "TrainingDirectionOrSpeciality");
    }

    /**
     * @return Ассоциированный массив форма обучения - лицензированный объем
     */
    @ElementCollection
    @CollectionTable(schema="uch")
    //Необходимый параметр для коллекции SortedMap
    @Sort(type=SortType.NATURAL)
    public SortedMap<EducationForm, Integer> getLicenseQuantityByEducationForm() {
        return licenseQuantityByEducationForm;
    }

    public void setLicenseQuantityByEducationForm(SortedMap<EducationForm, Integer> licenseQuantityByEducationForm) {
        this.licenseQuantityByEducationForm = licenseQuantityByEducationForm;
    }

//    public void addEduFormLicenseQuantity(EducationForm eduForm, Integer licenseQuantity){
//        this.licenseQuantityByEducationForm.put(eduForm, licenseQuantity);
//    }

    /**
     * @return дата окончания лицензионной записи
     */
    @Temporal(value = javax.persistence.TemporalType.DATE)
    @NotNull
    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = Defense.notNull(terminationDate, "terminationDate");
    }

    /**
     * @return название групп лицензионных записей (подготовка бакалавров, для колледжа и т.д.)
     */
    @ManyToOne
    @NotNull
    public LicenseRecordGroup getLicenseRecordGroup() {
        return licenseRecordGroup;
    }

    public void setLicenseRecordGroup(LicenseRecordGroup licenseRecordGroup) {
        this.licenseRecordGroup = Defense.notNull(licenseRecordGroup, "licenseRecordGroup");
    }

    /**
     * @return Организационная единица, подготавливающая студентов в рамках данной лицензионной записи
     */
    @ManyToOne
    @NotNull
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = Defense.notNull(orgUnit, "orgUnit");
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
        return o.toString().compareTo(o.toString());
    }
}
