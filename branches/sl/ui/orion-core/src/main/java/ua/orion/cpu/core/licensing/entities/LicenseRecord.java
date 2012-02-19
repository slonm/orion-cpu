package ua.orion.cpu.core.licensing.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.tapestry5.beaneditor.DataType;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Сущность записи в лицензии (даёт право на обучение студентов в соответствии с
 * указанными в записи атрибутами)
 *
 * @author kgp
 */
//  Невозможно создать ограничение, что-бы trainingDirection не повторялось при speciality=null
//  Но в таком случае записи бакалавров будут дублироваться
@Entity
@Table(uniqueConstraints = {
    //    @UniqueConstraint(columnNames = {"educationalQualificationLevel",
    //        "trainingDirection", "termination", "license"}),
    @UniqueConstraint(columnNames = {"educationalQualificationLevel",
        "speciality", "termination", "license"})
})
public class LicenseRecord extends AbstractEntity<LicenseRecord> {

    private License license;
    private TrainingDirection trainingDirection;
    private Speciality speciality;
    private EducationalQualificationLevel educationalQualificationLevel;
    //Создание пользовательского типа данных, указывающего на Property Block,
    //используемый в гриде и бинэдиторе
    @DataType("EduFormLicenseQuantity")
    private SortedMap<EducationForm, Integer> licenseQuantityByEducationForm = new TreeMap();
    private Calendar termination;
    private OrgUnit orgUnit;
    private LicenseRecordGroup licenseRecordGroup;
    @DataType("longtext")
    private String trainingVariants;
    //Подпись классификации в именительном падеже, единственном числе
    private String classify;

    public LicenseRecord() {
    }

    public LicenseRecord(
            License license,
            TrainingDirection trainingDirection,
            EducationalQualificationLevel educationalQualificationLevel,
            EducationForm statEduForm,
            int statLicenseQuantity,
            EducationForm corrEduForm,
            int corrLicenseQuantity,
            Calendar terminationDate,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {

        licenseQuantityByEducationForm.put(statEduForm, statLicenseQuantity);
        licenseQuantityByEducationForm.put(corrEduForm, corrLicenseQuantity);
        this.license = license;
        this.trainingDirection = trainingDirection;
        this.educationalQualificationLevel = educationalQualificationLevel;
        this.termination = terminationDate;
        this.orgUnit = orgUnit;
        this.licenseRecordGroup = licenseRecordGroup;
    }

    public LicenseRecord(
            License license,
            Speciality speciality,
            EducationalQualificationLevel educationalQualificationLevel,
            EducationForm statEduForm,
            int statLicenseQuantity,
            EducationForm corrEduForm,
            int corrLicenseQuantity,
            Calendar terminationDate,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {

        licenseQuantityByEducationForm.put(statEduForm, statLicenseQuantity);
        licenseQuantityByEducationForm.put(corrEduForm, corrLicenseQuantity);
        this.license = license;
        this.speciality = speciality;
        this.educationalQualificationLevel = educationalQualificationLevel;
        this.termination = terminationDate;
        this.orgUnit = orgUnit;
        this.licenseRecordGroup = licenseRecordGroup;
    }

    /**
     * Применение ограничений на взаимно-зависимые поля
     *
     */
    @PrePersist
    @PreUpdate
    public void preSave() {
        if (EducationalQualificationLevel.BACHELOR_UKEY.equals(educationalQualificationLevel.getUKey())) {
            speciality = null;
        } else {
            if (speciality != null) {
                trainingDirection = speciality.getTrainingDirection();
            } else {
                trainingDirection = null;
            }
        }
    }

    /**
     * @return Серия, номер и дата выдачи лицензии, к которой принадлежит данная
     * запись (не отображается в гриде)
     */
    @ManyToOne
    @NotNull
    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    @Transient
    public String getKnowledgeAreaCode() {
        try {
            return trainingDirection.getKnowledgeArea().getCode();
        } catch (NullPointerException e1) {
            return null;
        }
    }

    @Transient
    public String getKnowledgeAreaName() {
        try {
            return trainingDirection.getKnowledgeArea().getName();
        } catch (NullPointerException e1) {
            return null;
        }
    }

    /**
     * @return образовательно-квалификайционный уровень (не отображается в
     * гриде)
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "educationalQualificationLevel")
    public EducationalQualificationLevel getEducationalQualificationLevel() {
        return educationalQualificationLevel;
    }

    public void setEducationalQualificationLevel(EducationalQualificationLevel educationalQualificationLevel) {
        this.educationalQualificationLevel = educationalQualificationLevel;
    }

    @Transient
    public String getCode() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(educationalQualificationLevel.getCode());
            sb.append(".");
            sb.append(trainingDirection.getKnowledgeArea().getCode());
            sb.append(trainingDirection.getCode());
            if (speciality != null) {
                sb.append(speciality.getCode());
            }
            return sb.toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * @return название направления обучения/специальности
     */
    @ManyToOne
    @JoinColumn(name = "trainingDirection")
    @DataType("LicenseTrainingDirection")
    public TrainingDirection getTrainingDirection() {
        return trainingDirection;
    }

    public void setTrainingDirection(TrainingDirection trainingDirection) {
        this.trainingDirection = trainingDirection;
    }

    @ManyToOne
    @JoinColumn(name = "speciality")
    @DataType("LicenseSpeciality")
    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getTrainingVariants() {
        return trainingVariants;
    }

    public void setTrainingVariants(String trainingVariants) {
        this.trainingVariants = trainingVariants;
    }

    /**
     * @return Ассоциированный массив форма обучения - лицензированный объем
     */
    @ElementCollection
    @CollectionTable
    //Необходимый параметр для коллекции SortedMap
    @Sort(type = SortType.NATURAL)
    public SortedMap<EducationForm, Integer> getLicenseQuantityByEducationForm() {
        return licenseQuantityByEducationForm;
    }

    public void setLicenseQuantityByEducationForm(SortedMap<EducationForm, Integer> licenseQuantityByEducationForm) {
        this.licenseQuantityByEducationForm = licenseQuantityByEducationForm;
    }

    /**
     * @return дата окончания лицензионной записи
     */
    @Temporal(TemporalType.DATE)
    @NotNull
    public Calendar getTermination() {
        return termination;
    }

    public void setTermination(Calendar termination) {
        this.termination = termination;
    }

    /**
     * @return название групп лицензионных записей (подготовка бакалавров, для
     * колледжа и т.д.)
     */
    @ManyToOne
    @NotNull
    public LicenseRecordGroup getLicenseRecordGroup() {
        return licenseRecordGroup;
    }

    public void setLicenseRecordGroup(LicenseRecordGroup licenseRecordGroup) {
        this.licenseRecordGroup = licenseRecordGroup;
    }

    /**
     * @return Организационная единица, подготавливающая студентов в рамках
     * данной лицензионной записи
     */
    @ManyToOne
    @NotNull
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    @Override
    public String toString() {
        return getCode() + " - " + " - "
                + ((speciality != null) ? speciality : trainingDirection)
                + " (" + getLicenseRecordGroup() + ")";
    }

    @Override
    protected boolean entityEquals(LicenseRecord obj) {
        return aEqualsField(speciality, obj.speciality)
                && aEqualsField(trainingDirection, obj.trainingDirection)
                && aEqualsField(educationalQualificationLevel, obj.educationalQualificationLevel);
    }

    @Override
    public int compareTo(LicenseRecord o) {
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }
}
