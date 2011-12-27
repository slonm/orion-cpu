package ua.orion.cpu.core.employees.entities;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;
import ua.orion.cpu.core.persons.entities.*;

/**
 * Работник. Наследуется от персоны и содержит все необходимые поля для ведения
 * по данному объекту учета, как по сотруднику.
 *
 * @author sl
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Person {

    /**
     * serialVersionUID необходим для динамической проверки возможности десери-
     * ализации. Если у источника и приемника различные serialVersionUID, то
     * среда выполнения в праве считать, что это разные классы, и поэтому и вы-
     * бросит InvalidClassExceptions.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Должности, на которых работает сотрудник. Так как допускается совместит-
     * ельство, у сотрудника может быть набор должностей.
     */
    private Set<EmployeePost> employeePosts = new HashSet<EmployeePost>();
    /**
     * Ученая степень.
     */
    private ScientificDegree scientificDegree;
    /**
     * Область науки.
     */
    private ScienceArea scienceArea;
    /**
     * Ученое звание.
     */
    private AcademicRank academicRank;
    /**
     * Почетное звание. Строка.
     */
    private String titleHonour;
    /**
     * Количество научных работ.
     */
    private Integer quontityScientifyActivity;
    /**
     * Год, в котором было присвоено ученое звание.
     */
    private Integer yearAcceptRank;
    /**
     * Отдел, где работает сотрудник.
     */
    private OrgUnit codeDepartment;
    /**
     * Отдел увольнения.
     */
    private OrgUnit codeDepartmentDismission;
    /**
     * Семейный статус.
     */
    private FamilyStatus familyStatus;
    /**
     * Последнее место работы.
     */
    private String lastPlaceActivity;
    private TypeAgreement typeAgreement;
    private LabourRelations labourRelations;
    private Integer pedagogicalExperienceYear;
    private Integer pedagogicalExperienceMonth;
    private Integer totalExperienceYear;
    private Integer totalExperienceMonth;
    private Calendar beginContinousPedagogicalExirience;
    private String aspirant;
    private Calendar beginAspirant;
    private Calendar finishAspirant;
    private Retiree codeRetiree;
    private Calendar dateRetiree;
    private String numberPensionCertificate;
    private Calendar dateStartPeriodTempering;
    private Calendar dateEndPeriodTempering;
    private Calendar dateStartScheduledTempering;
    private Calendar dateEndScheduledTempering;

    /**
     * Создание объекта класса Сотрудник указывая все параметры(поля)
     *
     * @param scientificDegree - ученая степень(Справочник)
     * @param scienceArea - область науки(Справочник)
     * @param academicRank - ученое звание(Справочник)
     * @param titleHonour - почетное звание
     * @param quontityScientifyActivity - кол-во научных работ
     * @param yearAcceptRank - год присвоения звания
     * @param codeDepartment - отдел(Справочник)
     * @param codeDepartmentDismission - отдел увольнения
     * @param familyStatus - семейный статус(Справочник)
     * @param lastPlaceActivity - последнее место работы
     * @param typeAgreement - тип трудового договора(Справочник)
     * @param labourRelations - тип трудовых отношений(Справочник)
     * @param pedagogicalExperienceYear - педагогический стаж (года)
     * @param pedagogicalExperienceMonth - педагогический стаж (месяца)
     * @param totalExperienceYear - общий стаж (года)
     * @param totalExperienceMonth - общий стаж (месяца)
     * @param beginContinousPedagogicalExirience - начало беспрерывного педаго-
     * гического стажа в университете
     * @param aspirant - аспирантура
     * @param beginAspirant - дата начала аспирантуры
     * @param finishAspirant - дата окончания аспирантуры
     * @param codeRetiree - тип пенсии
     * @param dateRetiree - дата выхода на пенсию
     * @param numberPensionCertificate - номер пенсионного удостоверения
     * @param dateStartPeriodTempering - дата начала отпуска
     * @param dateEndPeriodTempering - дата окончания отпуска
     * @param dateStartScheduledTempering - дата начала планируемого отпуска
     * @param dateEndScheduledTempering - дата окончания планируемого отпуска
     * @param firstName - имя
     * @param patronymicName - отчество
     * @param surname - фамилия
     * @param birthday - дата рождения
     * @param sex - пол сотрудника(Справочник)
     * @param citizenship - гражданство(Справочник)
     * @param handle - идентификационный код
     * @param passport - пасспорт(Отдельная сущность)
     * @param datePhliugraphia - дата прохождения флюрографии
     * @param notes - заметки. То, что не относится ни к одному другому полю.
     */
    public Employee(ScientificDegree scientificDegree, ScienceArea scienceArea, AcademicRank academicRank, String titleHonour, Integer quontityScientifyActivity, Integer yearAcceptRank, OrgUnit codeDepartment, OrgUnit codeDepartmentDismission, FamilyStatus familyStatus, String lastPlaceActivity, TypeAgreement typeAgreement, LabourRelations labourRelations, Integer pedagogicalExperienceYear, Integer pedagogicalExperienceMonth, Integer totalExperienceYear, Integer totalExperienceMonth, Calendar beginContinousPedagogicalExirience, String aspirant, Calendar beginAspirant, Calendar finishAspirant, Retiree codeRetiree, Calendar dateRetiree, String numberPensionCertificate, Calendar dateStartPeriodTempering, Calendar dateEndPeriodTempering, Calendar dateStartScheduledTempering, Calendar dateEndScheduledTempering, String firstName, String patronymicName, String surname, Calendar birthday, Sex sex, Citizenship citizenship, String handle, Passport passport, Calendar datePhliugraphia, String notes) {
        super(firstName, patronymicName, surname, birthday, sex, citizenship, handle, passport, datePhliugraphia, notes);
        this.scientificDegree = scientificDegree;
        this.scienceArea = scienceArea;
        this.academicRank = academicRank;
        this.titleHonour = titleHonour;
        this.quontityScientifyActivity = quontityScientifyActivity;
        this.yearAcceptRank = yearAcceptRank;
        this.codeDepartment = codeDepartment;
        this.codeDepartmentDismission = codeDepartmentDismission;
        this.familyStatus = familyStatus;
        this.lastPlaceActivity = lastPlaceActivity;
        this.typeAgreement = typeAgreement;
        this.labourRelations = labourRelations;
        this.pedagogicalExperienceYear = pedagogicalExperienceYear;
        this.pedagogicalExperienceMonth = pedagogicalExperienceMonth;
        this.totalExperienceYear = totalExperienceYear;
        this.totalExperienceMonth = totalExperienceMonth;
        this.beginContinousPedagogicalExirience = beginContinousPedagogicalExirience;
        this.aspirant = aspirant;
        this.beginAspirant = beginAspirant;
        this.finishAspirant = finishAspirant;
        this.codeRetiree = codeRetiree;
        this.dateRetiree = dateRetiree;
        this.numberPensionCertificate = numberPensionCertificate;
        this.dateStartPeriodTempering = dateStartPeriodTempering;
        this.dateEndPeriodTempering = dateEndPeriodTempering;
        this.dateStartScheduledTempering = dateStartScheduledTempering;
        this.dateEndScheduledTempering = dateEndScheduledTempering;
    }

    /**
     * Вспомогательный конструктор,который позволяет создать объект с минималь-
     * ным количеством параметров. Для создания объекта пользователем в нем не-
     * обходимости нет. Но, при создании объекта в скрипте, это значительно уп-
     * рощает задачу.
     *
     * @param scientificDegree - ученая степень
     * @param scienceArea - область науки
     * @param academicRank - ученое звание
     * @param firstName - имя
     * @param patronymicName - отчество
     * @param surname - фамилия
     * @param birthday - дата рождения
     * @param sex - пол сотрудника
     * @param citizenship - гражданство(Справочник)
     * @param handle - идентификационный номер
     * @param passport - пасспорт(Хранится в другой сущности)
     * @param datePhliugraphia - дата прохождения флюрографии(Календарь)
     * @param notes - заметки. Дополнительная информация о человеке, которая не
     * вошла не в один из параметров.
     */
    public Employee(ScientificDegree scientificDegree, ScienceArea scienceArea, AcademicRank academicRank, String firstName, String patronymicName, String surname, Calendar birthday, Sex sex, Citizenship citizenship, String handle, Passport passport, Calendar datePhliugraphia, String notes) {
        super(firstName, patronymicName, surname, birthday, sex, citizenship, handle, passport, datePhliugraphia, notes);
        this.scientificDegree = scientificDegree;
        this.scienceArea = scienceArea;
        this.academicRank = academicRank;
    }

    public Employee() {
    }

    /**
     * Должности на которых работает работник
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    public Set<EmployeePost> getEmployeePosts() {
        return employeePosts;
    }

    public void setEmployeePosts(Set<EmployeePost> employeePosts) {
        this.employeePosts = employeePosts;
    }

    /*
     * TODO Добавить валидатор, указывающий на недопустимость пустого значения
     * scientificDegree при непустом scienceArea и наоборот пустого scienceArea
     * при непустом scientificDegree if(!scientificDegree.equals(null) ^
     * !(scienceArea)) {нельзя!!!}
     */
    @ManyToOne
    public ScientificDegree getScientificDegree() {
        return scientificDegree;
    }

    public void setScientificDegree(ScientificDegree scientificDegree) {
        this.scientificDegree = scientificDegree;
    }

    @ManyToOne
    public ScienceArea getScienceArea() {
        return scienceArea;
    }

    public void setScienceArea(ScienceArea scienceArea) {
        this.scienceArea = scienceArea;
    }

    @ManyToOne
    public AcademicRank getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(AcademicRank academicRank) {
        this.academicRank = academicRank;
    }

    @Transient
    public String getSciDegreeSciAreaFull() {
        if (scientificDegree != null && scienceArea != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(scientificDegree);
            sb.append(" ");
            sb.append(scienceArea);
            return sb.toString();
        }
        return "";
    }

    /**
     * Аспирантура
     *
     * @return
     */
    public String getAspirant() {
        return aspirant;
    }

    public void setAspirant(String aspirant) {
        this.aspirant = aspirant;
    }

    /**
     * Дата начала обучения в аспирантуре
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getBeginAspirant() {
        return beginAspirant;
    }

    public void setBeginAspirant(Calendar beginAspirant) {
        this.beginAspirant = beginAspirant;
    }

    /**
     * Дата окончания аспирантуры
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getFinishAspirant() {
        return finishAspirant;
    }

    public void setFinishAspirant(Calendar finishAspirant) {
        this.finishAspirant = finishAspirant;
    }

    /**
     * Начало беспрерывного педагогического стажа в ВУЗе
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getBeginContinousPedagogicalExirience() {
        return beginContinousPedagogicalExirience;
    }

    public void setBeginContinousPedagogicalExirience(Calendar beginContinousPedagogicalExirience) {
        this.beginContinousPedagogicalExirience = beginContinousPedagogicalExirience;
    }

    /**
     * Педагогический стаж (месяцы)
     *
     * @return
     */
    public Integer getPedagogicalExperienceMonth() {
        return pedagogicalExperienceMonth;
    }

    public void setPedagogicalExperienceMonth(Integer pedagogicalExperienceMonth) {
        this.pedagogicalExperienceMonth = pedagogicalExperienceMonth;
    }

    /**
     * Педагогический стаж (года)
     *
     * @return
     */
    public Integer getPedagogicalExperienceYear() {
        return pedagogicalExperienceYear;
    }

    public void setPedagogicalExperienceYear(Integer pedagogicalExperienceYear) {
        this.pedagogicalExperienceYear = pedagogicalExperienceYear;
    }

    /**
     * Общий стаж работы (месяцы)
     *
     * @return
     */
    public Integer getTotalExperienceMonth() {
        return totalExperienceMonth;
    }

    public void setTotalExperienceMonth(Integer totalExperienceMonth) {
        this.totalExperienceMonth = totalExperienceMonth;
    }

    /**
     * Общий стаж работы (года)
     *
     * @return
     */
    public Integer getTotalExperienceYear() {
        return totalExperienceYear;
    }

    public void setTotalExperienceYear(Integer totalExperienceYear) {
        this.totalExperienceYear = totalExperienceYear;
    }

    /**
     * Отдел
     *
     * @return
     */
    @ManyToOne
    public OrgUnit getCodeDepartment() {
        return codeDepartment;
    }

    public void setCodeDepartment(OrgUnit codeDepartment) {
        this.codeDepartment = codeDepartment;
    }

    /**
     * Отдел увольнения
     *
     * @return
     */
    @ManyToOne
    public OrgUnit getCodeDepartmentDismission() {
        return codeDepartmentDismission;
    }

    public void setCodeDepartmentDismission(OrgUnit codeDepartmentDismission) {
        this.codeDepartmentDismission = codeDepartmentDismission;
    }

    /**
     * Семейный статус
     *
     * @return
     */
    @ManyToOne
    public FamilyStatus getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(FamilyStatus familyStatus) {
        this.familyStatus = familyStatus;
    }

    /**
     * Вид трудовых отношений
     *
     * @return
     */
    @ManyToOne
    public LabourRelations getLabourRelations() {
        return labourRelations;
    }

    public void setLabourRelations(LabourRelations labourRelations) {
        this.labourRelations = labourRelations;
    }

    /**
     * Последнее место работы
     *
     * @return
     */
    public String getLastPlaceActivity() {
        return lastPlaceActivity;
    }

    public void setLastPlaceActivity(String lastPlaceActivity) {
        this.lastPlaceActivity = lastPlaceActivity;
    }

    /**
     * Кол-во научных работ
     *
     * @return
     */
    public Integer getQuontityScientifyActivity() {
        return quontityScientifyActivity;
    }

    public void setQuontityScientifyActivity(Integer quontityScientifyActivity) {
        this.quontityScientifyActivity = quontityScientifyActivity;
    }

    /**
     * Почетное звание
     *
     * @return
     */
    public String getTitleHonour() {
        return titleHonour;
    }

    public void setTitleHonour(String titleHonour) {
        this.titleHonour = titleHonour;
    }

    /**
     * Тип трудового договора
     *
     * @return
     */
    @ManyToOne
    public TypeAgreement getTypeAgreement() {
        return typeAgreement;
    }

    public void setTypeAgreement(TypeAgreement typeAgreement) {
        this.typeAgreement = typeAgreement;
    }

    /**
     * Год присвоения ученного звания
     *
     * @return
     */
    public Integer getYearAcceptRank() {
        return yearAcceptRank;
    }

    public void setYearAcceptRank(Integer yearAcceptRank) {
        this.yearAcceptRank = yearAcceptRank;
    }

    /**
     * Тип пенсии
     *
     * @return
     */
    @ManyToOne
    public Retiree getCodeRetiree() {
        return codeRetiree;
    }

    public void setCodeRetiree(Retiree codeRetiree) {
        this.codeRetiree = codeRetiree;
    }

    /**
     * Дата начала отпуска
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDateStartPeriodTempering() {
        return dateStartPeriodTempering;
    }

    public void setDateStartPeriodTempering(Calendar dateStartPeriodTempering) {
        this.dateStartPeriodTempering = dateStartPeriodTempering;
    }

    /**
     * Дата окончания отпуска
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDateEndPeriodTempering() {
        return dateEndPeriodTempering;
    }

    public void setDateEndPeriodTempering(Calendar dateEndPeriodTempering) {
        this.dateEndPeriodTempering = dateEndPeriodTempering;
    }

    /**
     * Дата начала планируемого отпуска
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDateStartScheduledTempering() {
        return dateStartScheduledTempering;
    }

    public void setDateStartScheduledTempering(Calendar dateStartScheduledTempering) {
        this.dateStartScheduledTempering = dateStartScheduledTempering;
    }

    /**
     * Дата окончания планируемого отпуска
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDateEndScheduledTempering() {
        return dateEndScheduledTempering;
    }

    public void setDateEndScheduledTempering(Calendar dateEndScheduledTempering) {
        this.dateEndScheduledTempering = dateEndScheduledTempering;
    }

    /**
     * Дата выхода на пенсию
     *
     * @return
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Calendar getDateRetiree() {
        return dateRetiree;
    }

    public void setDateRetiree(Calendar dateRetiree) {
        this.dateRetiree = dateRetiree;
    }

    /**
     * Номер пенсионного удостоверения
     *
     * @return
     */
    public String getNumberPensionCertificate() {
        return numberPensionCertificate;
    }

    public void setNumberPensionCertificate(String numberPensionCertificate) {
        this.numberPensionCertificate = numberPensionCertificate;
    }

    @Transient
    @Override
    public String getName() {
        return getFullNameRu();
    }

    @Override
    public void setName(String name) {
    }

    @Transient
    public String getSciDegreeSciAreaShort() {
        try {
            if (scientificDegree.getShortName() != null && scienceArea.getShortName() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(scientificDegree.getShortName());
                sb.append(scienceArea.getShortName());
                return sb.toString();
            }
        } catch (Exception e) {
        }
        return "";
    }
}
