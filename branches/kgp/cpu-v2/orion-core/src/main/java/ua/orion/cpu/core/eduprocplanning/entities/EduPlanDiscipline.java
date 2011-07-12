package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.utils.Defense;

/**
 * Сущность дисциплина учебного плана (может читаться в нескольких семестрах)
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlanDiscipline extends AbstractEntity<EduPlanDiscipline> {

    private static long serialVersionUID = 1L;
    
    //является ли дисциплина обязательной или выборочной (она может принадлежать 
    //как к нормативным, так и к выборочным циклас)
    private Boolean isMandatory;
    //Номер дисциплины в учебном плане (берётся из ОПП)
    private String disciplineNumber;
    //Название дисциплины (берётся из справочника)
    private Discipline discipline;
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
    //Количество часов лекций по дисциплине
    private Integer lecturesHours;
    // Количество часов лабораторных работ по дисциплине
    private Integer labsHours;
    //Количество часов практических занятий, семинаров по дисциплине
    private Integer practicesHours;

    //ПОКА НЕ ИСПОЛЬЗУЮ
    //Набор дисциплин данного учебного плана, которые должны предшествовать изучению данной дисциплины
//    private Set<EduPlanDiscipline> eduPlanDisciplinePrevious = new HashSet<EduPlanDiscipline>();
    
    public EduPlanDiscipline() {
    }

     public EduPlanDiscipline(Boolean isMandatory, 
             String disciplineNumber, Discipline discipline, Double ectsCreditAmount, String examSemestr, 
             String creditSemester, String courseWorkSemester, String controlWorkSemester, 
             Integer lecturesHours, Integer labsHours, Integer practicesHours) {
        this.isMandatory = isMandatory;
        this.disciplineNumber = disciplineNumber;
        this.discipline = discipline;
        this.ectsCreditAmount = ectsCreditAmount;
        this.examSemestr = examSemestr;
        this.creditSemester = creditSemester;
        this.courseWorkSemester = courseWorkSemester;
        this.controlWorkSemester = controlWorkSemester;
        this.lecturesHours = lecturesHours;
        this.labsHours = labsHours;
        this.practicesHours = practicesHours;
    }
    
    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getDisciplineNumber() {
        return disciplineNumber;
    }

    public void setDisciplineNumber(String disciplineNumber) {
        this.disciplineNumber = disciplineNumber;
    }

    //Однонаправленная ассоциация со справочником дисциплин (Названий дисциплин)
    @ManyToOne
    @JoinColumn(nullable = false)
    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = Defense.notNull(discipline, "discipline");
    }

    /**
     * Геттер для отображения общего количества часов дисциплины в гриде 
     * (сеттер для вычислимого поля, как и само поле, не нужны)
     * 
     * "Натяжка" предметной области: кредиты могут тиеть значение с кратносью 0,25,
     * а число часов дисциплины должно быть целым
     * При количестве часов в одном кредите, кратном 4, всё будет ОК
     * Надеемся на мудрость Министерства образования
     * @return  целое число часов, соответсвующих общему числу кредитов дисциплины
     */
    @Transient
    public Integer getTotalHours() {
        return (int) (ectsCreditAmount*NormativeValue.ECTSCREDIT);
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
    
    /**
     * Геттер для отображения часов самостоятельной работы студента по дисциплине в гриде 
     * (сеттер для вычислимого поля, как и само поле, не нужны)
     */
    @Transient
    public Integer getStudentIndependWorkHours() {
        return this.getTotalHours()-lecturesHours-labsHours-practicesHours;
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
        String eduPlan = "<NULL>";
        try {
            name = this.discipline.getName();
        } catch (NullPointerException e) {
        }
        try {
//            eduPlan = eduPlanDisciplineCycle.getEduPlan().toString();
        } catch (NullPointerException e) {
        }
//        return name + " " + eduPlan;
        return name;
    }

    @Override
    protected boolean entityEquals(EduPlanDiscipline obj) {
//        return aEqualsField(this.discipline.getName(), obj.discipline.getName()) && aEqualsField(eduPlanDisciplineCycle.getEduPlan(), obj.eduPlanDisciplineCycle.getEduPlan());
        return aEqualsField(this.discipline.getName(), obj.discipline.getName());
    }

    @Override
    public int compareTo(EduPlanDiscipline o) {
        return o.toString().compareTo(o.toString());
    }
}
