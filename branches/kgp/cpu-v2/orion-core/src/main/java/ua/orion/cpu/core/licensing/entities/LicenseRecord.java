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
import ua.orion.core.utils.Defense;

/**
 * Сущность записи в лицензии (даёт право на обучение студентов в соответствии 
 * с указанными в записи атрибутами)
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class LicenseRecord extends AbstractEntity<LicenseRecord> {

    private License license;
    private TrainingDirectionOrSpeciality trainingDirectionOrSpeciality;
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
    private String knowledgeAreaOrTrainingDirectionName;
    //Добавляем свойство код области знаний/направления обучения для обеспечения 
    //выборки с использованием @Formula, что нужно для сортировки по этому полю
    private String knowledgeAreaOrTrainingDirectionCode;
    //Добавляем свойство составной шифр лицензионной записи для обеспечения 
    //выборки с использованием @Formula, что нужно для сортировки по этому полю
    private String code;

    public LicenseRecord() {
    }

    public LicenseRecord(License license,
            TrainingDirectionOrSpeciality trainingDirectionOrSpeciality,
            EducationalQualificationLevel educationalQualificationLevel,
            SortedMap<EducationForm, Integer> licenseQuantityByEducationForm,
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

    //Вычислимое поле - выборкка строк из базы данных с помощью @Formula 
    //для обеспечения сортировки по этому полю
    @Formula("(select katd.code from ref.Training_Direction_Or_Speciality tds "
    + "join ref.knowledge_Area_Or_Training_Direction katd on tds.knowledge_Area_Or_Training_Direction=katd.id "
    + "where tds.id=training_Direction_Or_Speciality)")
    public String getKnowledgeAreaOrTrainingDirectionCode() {
        return knowledgeAreaOrTrainingDirectionCode;
    }

    public void setKnowledgeAreaOrTrainingDirectionCode(String knowledgeAreaOrTrainingDirectionCode) {
        this.knowledgeAreaOrTrainingDirectionCode = knowledgeAreaOrTrainingDirectionCode;
    }

    //Вычислимое поле - выборкка строк из базы данных с помощью @Formula 
    //для обеспечения сортировки по этому полю
    @Formula("(select katd.name from ref.Training_Direction_Or_Speciality tds "
    + "join ref.knowledge_Area_Or_Training_Direction katd on tds.knowledge_Area_Or_Training_Direction=katd.id "
    + "where tds.id=training_Direction_Or_Speciality)")
    public String getKnowledgeAreaOrTrainingDirectionName() {
        return knowledgeAreaOrTrainingDirectionName;
    }

    public void setKnowledgeAreaOrTrainingDirectionName(String knowledgeAreaOrTrainingDirectionName) {
        this.knowledgeAreaOrTrainingDirectionName = knowledgeAreaOrTrainingDirectionName;
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
        return o == null ? -1 : toString().compareToIgnoreCase(o.toString());
    }
}
