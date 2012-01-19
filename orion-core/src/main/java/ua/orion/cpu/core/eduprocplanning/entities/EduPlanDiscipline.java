package ua.orion.cpu.core.eduprocplanning.entities;

import java.util.*;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.utils.Defense;

/**
 * Сущность дисциплина учебного плана (может читаться в нескольких семестрах)
 *
 * @author kgp
 */
@Entity
@Table
public class EduPlanDiscipline extends AbstractEntity<EduPlanDiscipline> {

    private static long serialVersionUID = 1L;
    //Поле связи с учебным планом, к которому принадлежит дисциплина
    private EduPlan eduPlan;
    //является дисциплина обязательной, выборочной или факультативной
    private EduPlanDisciplineVariant disciplineVariant;
    //Номер дисциплины в учебном плане (берётся из ОПП)
    private String disciplineNumber;
    //Название дисциплины (берётся из справочника)
    private Set<Discipline> disciplines;
    //TODO НА ФОРМЕ ВВОДА КОЛИЧЕСТВА КРЕДИТОВ ОГРАНИЧИТЬ ЗНАЧЕНИЯ 
    //ТОЛЬКО С ДРОБНОЙ ЧАСТЬЮ .0 ИЛИ .25  ИЛИ .5 ИЛИ .75 
    //Количество кредитов ECTS для общего объема часов дисциплины 
    private Double ectsCreditAmount;
    //TODO Написать валидатор для ввода
    //№ семестра, в котором проводится экзамен по дисциплине (если в нескольких семестрах,
    //то они перечисляются арабскими цифрами через запятую или тире)
    private String examSemestr;
    //№ семестра, в котором проводится зачет по дисциплине (если в нескольких семестрах,
    //то они перечисляются арабскими цифрами через запятую или тире)
    private String creditSemester;
    //№ семестра, в котором проводится курсовая работа по дисциплине (если в нескольких семестрах,
    //то они перечисляются арабскими цифрами через запятую или тире)
    private String courseWorkSemester;
    //№ семестра, в котором проводится контрольная работа по дисциплине (если в нескольких семестрах,
    //то они перечисляются арабскими цифрами через запятую или тире)
    private String controlWorkSemester;
    //Кол-во экзаменов
    private Integer examSemestrQuantity;
    //Кол-во зачетов
    private Integer creditSemesterQuantity;
    //Кол-во курсовых работ
    private Integer courseWorkSemesterQuantity;
    //Кол-во контрольных работ
    private Integer controlWorkSemesterQuantity;
    //Количество часов лекций по дисциплине
    private Integer lecturesHours;
    // Количество часов лабораторных работ по дисциплине
    private Integer labsHours;
    //Количество часов практических занятий, семинаров по дисциплине
    private Integer practicesHours;
    //Теги дисциплины
    private Set<EduPlanDisciplineTag> eduPlanDisciplineTags = new HashSet();

    //ПОКА НЕ ИСПОЛЬЗУЮ
    //Набор дисциплин данного учебного плана, которые должны предшествовать изучению данной дисциплины
//    private Set<EduPlanDiscipline> eduPlanDisciplinePrevious = new HashSet<EduPlanDiscipline>();
    public EduPlanDiscipline() {
    }

    public EduPlanDiscipline(EduPlan eduPlan, EduPlanDisciplineVariant disciplineVariant,
            String disciplineNumber, Set<Discipline> disciplines, Double ectsCreditAmount, String examSemestr,
            String creditSemester, String courseWorkSemester, String controlWorkSemester,
            Integer examSemestrQuantity, Integer creditSemesterQuantity,
            Integer courseWorkSemesterQuantity, Integer controlWorkSemesterQuantity,
            Integer lecturesHours, Integer labsHours, Integer practicesHours) {
        this.eduPlan = eduPlan;
        this.disciplineVariant = disciplineVariant;
        this.disciplineNumber = disciplineNumber;
        this.disciplines = disciplines;
        this.ectsCreditAmount = ectsCreditAmount;
        this.examSemestr = examSemestr;
        this.creditSemester = creditSemester;
        this.courseWorkSemester = courseWorkSemester;
        this.controlWorkSemester = controlWorkSemester;
        this.examSemestrQuantity = examSemestrQuantity;
        this.creditSemesterQuantity = creditSemesterQuantity;
        this.courseWorkSemesterQuantity = courseWorkSemesterQuantity;
        this.controlWorkSemesterQuantity = controlWorkSemesterQuantity;
        this.lecturesHours = lecturesHours;
        this.labsHours = labsHours;
        this.practicesHours = practicesHours;
    }

    
    
    //Двунаправленная ассоциация с планом, к которому принадлежит данная дисциплина
    @ManyToOne
    public EduPlan getEduPlan() {
        return eduPlan;
    }

    public void setEduPlan(EduPlan eduPlan) {
        this.eduPlan = eduPlan;
    }

    public EduPlanDisciplineVariant getDisciplineVariant() {
        return disciplineVariant;
    }

    public void setDisciplineVariant(EduPlanDisciplineVariant disciplineVariant) {
        this.disciplineVariant = disciplineVariant;
    }

    public String getDisciplineNumber() {
        return disciplineNumber;
    }

    public void setDisciplineNumber(String disciplineNumber) {
        this.disciplineNumber = disciplineNumber;
    }

    @OneToMany
 
    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    /**
     * Геттер для отображения общего количества часов дисциплины в гриде (сеттер
     * для вычислимого поля, как и само поле, не нужны)
     *
     * "Натяжка" предметной области: кредиты могут тиеть значение с кратносью
     * 0,25, а число часов дисциплины должно быть целым При количестве часов в
     * одном кредите, кратном 4, всё будет ОК Надеемся на мудрость Министерства
     * образования
     *
     * @return целое число часов, соответсвующих общему числу кредитов
     * дисциплины
     */
    @Transient
    public Integer getTotalHours() {
        return (int) (ectsCreditAmount * NormativeValue.ECTSCREDIT);
    }

    public Double getEctsCreditAmount() {
        return ectsCreditAmount;
    }

    public void setEctsCreditAmount(Double ectsCreditAmount) {
        this.ectsCreditAmount = ectsCreditAmount;
    }

    public String getExamSemestr() {
        return examSemestr;
    }

    public void setExamSemestr(String examSemestr) {
        this.examSemestr = examSemestr;
    }

    public String getCreditSemester() {
        return creditSemester;
    }

    public void setCreditSemester(String creditSemester) {
        this.creditSemester = creditSemester;
    }

    public String getCourseWorkSemester() {
        return courseWorkSemester;
    }

    public void setCourseWorkSemester(String courseWorkSemester) {
        this.courseWorkSemester = courseWorkSemester;
    }

    public String getControlWorkSemester() {
        return controlWorkSemester;
    }

    public void setControlWorkSemester(String controlWorkSemester) {
        this.controlWorkSemester = controlWorkSemester;
    }

    public Integer getLecturesHours() {
        return lecturesHours;
    }

    public void setLecturesHours(Integer lecturesHours) {
        this.lecturesHours = lecturesHours;
    }

    public Integer getLabsHours() {
        return labsHours;
    }

    public void setLabsHours(Integer labsHours) {
        this.labsHours = labsHours;
    }

    public Integer getPracticesHours() {
        return practicesHours;
    }

    public void setPracticesHours(Integer practicesHours) {
        this.practicesHours = practicesHours;
    }

    public Integer getControlWorkSemesterQuantity() {
        return controlWorkSemesterQuantity;
    }

    public void setControlWorkSemesterQuantity(Integer controlWorkSemesterQuantity) {
        this.controlWorkSemesterQuantity = controlWorkSemesterQuantity;
    }

    public Integer getCourseWorkSemesterQuantity() {
        return courseWorkSemesterQuantity;
    }

    public void setCourseWorkSemesterQuantity(Integer courseWorkSemesterQuantity) {
        this.courseWorkSemesterQuantity = courseWorkSemesterQuantity;
    }

    public Integer getCreditSemesterQuantity() {
        return creditSemesterQuantity;
    }

    public void setCreditSemesterQuantity(Integer creditSemesterQuantity) {
        this.creditSemesterQuantity = creditSemesterQuantity;
    }

    public Integer getExamSemestrQuantity() {
        return examSemestrQuantity;
    }

    public void setExamSemestrQuantity(Integer examSemestrQuantity) {
        this.examSemestrQuantity = examSemestrQuantity;
    }

    /**
     * Геттер для отображения часов самостоятельной работы студента по
     * дисциплине в гриде (сеттер для вычислимого поля, как и само поле, не
     * нужны)
     */
    @Transient
    public Integer getStudentIndependWorkHours() {
        return this.getTotalHours() - lecturesHours - labsHours - practicesHours;
    }

    //Двунаправленная ассоциация с тегами дисциплин
    @ManyToMany
    @JoinTable(joinColumns = {
        @JoinColumn(name = "EDUPLAN_DISCIPLINE")}, inverseJoinColumns = {
        @JoinColumn(name = "EDUPLAN_DISCIPLINE_Tag")})
    public Set<EduPlanDisciplineTag> getEduPlanDisciplineTags() {
        return eduPlanDisciplineTags;
    }

    public void setEduPlanDisciplineTags(Set<EduPlanDisciplineTag> eduPlanDisciplineTags) {
        this.eduPlanDisciplineTags = eduPlanDisciplineTags;
    }

//    /**
//     * @return Набор дисциплин, которые обязательно должны предшествовать изучению данной дисциплины
//     */
//    public Set<EduPlanDiscipline> getEduPlanDisciplinePrevious() {
//        return eduPlanDisciplinePrevious;
//    }
//
//    public void setEduPlanDisciplinePrevious(Set<EduPlanDiscipline> eduPlanDisciplinePrevious) {
//        this.eduPlanDisciplinePrevious = eduPlanDisciplinePrevious;
//    }
    @Override
    public String toString() {
        String name = "<NULL>";
        String ePlan = "<NULL>";
        try {
            name = this.disciplines.toString();
        } catch (NullPointerException e) {
        }
        try {
            ePlan = eduPlan.toString();
        } catch (NullPointerException e) {
        }
        return name + " " + ePlan;
    }

    @Override
    protected boolean entityEquals(EduPlanDiscipline obj) {
        return aEqualsField(this.disciplines.hashCode(), obj.disciplines.hashCode()) && aEqualsField(eduPlan, obj.eduPlan);
    }

    @Override
    public int compareTo(EduPlanDiscipline o) {
        return o.toString().compareTo(o.toString());
    }
}
