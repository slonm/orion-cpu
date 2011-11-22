package ua.orion.cpu.core.licensing.entities;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.tapestry5.beaneditor.DataType;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Сущность записи в лицензии (даёт право на обучение студентов в соответствии 
 * с указанными в записи атрибутами)
 * @author kgp
 */
@Entity
@Table(schema = "uch", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"educationalQualificationLevel",
        "trainingDirection", "termination", "license"}),
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
    //Добавляем свойство название области знаний/направления обучения для обеспечения 
    //выборки с использованием @Formula, что нужно для сортировки по этому полю
    private String knowledgeAreaName;
    //Добавляем свойство код области знаний/направления обучения для обеспечения 
    //выборки с использованием @Formula, что нужно для сортировки по этому полю
    private String knowledgeAreaCode;
    //Добавляем свойство составной шифр лицензионной записи для обеспечения 
    //выборки с использованием @Formula, что нужно для сортировки по этому полю
    private String code;

    public LicenseRecord() {
    }

    public LicenseRecord(License license,
            TrainingDirection trainingDirectionOrSpeciality,
            EducationalQualificationLevel educationalQualificationLevel,
            SortedMap<EducationForm, Integer> licenseQuantityByEducationForm,
            Calendar termination,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {
        this.license = license;
        this.trainingDirection = trainingDirectionOrSpeciality;
        this.educationalQualificationLevel = educationalQualificationLevel;
        this.licenseQuantityByEducationForm = licenseQuantityByEducationForm;
        this.termination = termination;
        this.orgUnit = orgUnit;
        this.licenseRecordGroup = licenseRecordGroup;
    }

    public LicenseRecord(License license,
            Speciality speciality,
            EducationalQualificationLevel educationalQualificationLevel,
            SortedMap<EducationForm, Integer> licenseQuantityByEducationForm,
            Calendar termination,
            OrgUnit orgUnit,
            LicenseRecordGroup licenseRecordGroup) {
        this.license = license;
        this.speciality = speciality;
        this.educationalQualificationLevel = educationalQualificationLevel;
        this.licenseQuantityByEducationForm = licenseQuantityByEducationForm;
        this.termination = termination;
        this.orgUnit = orgUnit;
        this.licenseRecordGroup = licenseRecordGroup;
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
        this.license = license;
    }

    //Вычислимое поле - выборкка строк из базы данных с помощью @Formula 
    //для обеспечения сортировки по этому полю
    @Formula("(select katd.code from ref.Training_Direction_Or_Speciality tds "
    + "join ref.knowledge_Area_Or_Training_Direction katd on tds.knowledge_Area_Or_Training_Direction=katd.id "
    + "where tds.id=training_Direction_Or_Speciality)")
    public String getKnowledgeAreaOrTrainingDirectionCode() {
        return knowledgeAreaCode;
    }

    public void setKnowledgeAreaOrTrainingDirectionCode(String knowledgeAreaOrTrainingDirectionCode) {
        this.knowledgeAreaCode = knowledgeAreaOrTrainingDirectionCode;
    }

    //Вычислимое поле - выборкка строк из базы данных с помощью @Formula 
    //для обеспечения сортировки по этому полю
    @Formula("(select katd.name from ref.Training_Direction_Or_Speciality tds "
    + "join ref.knowledge_Area_Or_Training_Direction katd on tds.knowledge_Area_Or_Training_Direction=katd.id "
    + "where tds.id=training_Direction_Or_Speciality)")
    public String getKnowledgeAreaOrTrainingDirectionName() {
        return knowledgeAreaName;
    }

    public void setKnowledgeAreaOrTrainingDirectionName(String knowledgeAreaOrTrainingDirectionName) {
        this.knowledgeAreaName = knowledgeAreaOrTrainingDirectionName;
    }

    /**
     * @return образовательно-квалификайционный уровень
     * (не отображается в гриде)
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

    //Вычислимое поле - выборкка строк из базы данных с помощью @Formula 
    //для обеспечения сортировки по этому полю
    @Formula("(select eql.code||'.'||katd.code||tds.code from ref.educational_qualification_level eql, ref.Training_Direction_Or_Speciality tds "
    + "join ref.knowledge_Area_Or_Training_Direction katd on tds.knowledge_Area_Or_Training_Direction=katd.id "
    + "where tds.id=training_Direction_Or_Speciality and educational_qualification_level=eql.id)")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return название направления обучения/специальности
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "trainingDirection")
    public TrainingDirection getTrainingDirection() {
        if (speciality != null) {
            return speciality.getTrainingDirection();
        }
        return trainingDirection;
    }

    public void setTrainingDirection(TrainingDirection trainingDirection) {
        this.trainingDirection = trainingDirection;
    }

    @ManyToOne
    @NotNull
    @JoinColumn(name = "speciality")
    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    /**
     * @return Ассоциированный массив форма обучения - лицензированный объем
     */
    @ElementCollection
    @CollectionTable(schema = "uch")
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
     * @return название групп лицензионных записей (подготовка бакалавров, для колледжа и т.д.)
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
     * @return Организационная единица, подготавливающая студентов в рамках данной лицензионной записи
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
        return getCode() + " - " + getKnowledgeAreaOrTrainingDirectionName() + " - "
                + getTrainingDirection() + " (" + getLicenseRecordGroup() + ")";
    }

    @Override
    protected boolean entityEquals(LicenseRecord obj) {
        return aEqualsField(getTrainingDirection(), obj.getTrainingDirection())
                && aEqualsField(educationalQualificationLevel, obj.educationalQualificationLevel);
    }

    @Override
    public int compareTo(LicenseRecord o) {
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }
}
