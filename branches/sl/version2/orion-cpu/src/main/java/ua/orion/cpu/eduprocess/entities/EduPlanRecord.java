package ua.orion.cpu.eduprocess.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.tapestry5.beaneditor.NonVisual;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Запись учебного плана (подразумевается рабочий план),
 * т.е. если дисциплина читается в нескольких семестрах,
 * будет несколько объектов класса EduPlanRecord.
 * Рассчитываемые поля не выводятся, они будут в представлении для грида
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlanRecord extends AbstractEntity<EduPlanRecord> {

    /**
     * № дисциплины в учебном плане (для нормативных берется из ОПП)
     */
    private String disciplineNumber;
    /**
     * Дисциплина учебного плана с указанием названия дисциплины, названия
     * и номера цикла и является ли она обязательной
     */
    private EduPlanDiscipline eduPlanDiscipline;
    /**
     * Семестр, в котором изучается дисциплина с номером семестра
     */
    private EduPlanSemester eduPlanSemester;
    /**
     * Есть ли экзамен по дисциплине в данном семестре
     */
    private Boolean isExam;
    /**
     * Есть ли зачет по дисциплине в данном семестре
     */
    private Boolean isCredit;
    /**
     * Есть ли курсовая работа по дисциплине в данном семестре
     */
    private Boolean isCourseWork;
    /**
     * Есть ли контрольная работа по дисциплине в данном семестре
     */
    private Boolean isControlWork;
    /**
     * Количество общих часов на дисциплину, указанное в данной записи
     * сумма таких часов по всем семестрам в которых читается дисциплина,
     * должна равняться EduPlanDiscipline.disciplineTotalHoursVolume
     */
    private Integer disciplineTotalHoursPerRecord;
    /**
     * Количество кредитов ECTS для общего объема часов дисциплины (РАССЧИТЫВАЕМОЕ ПОЛЕ)
     */
//    private Double ectsCreditAmount;
    /**
     * Количество часов лекций по дисциплине в данном семестре
     */
    private Integer lecturesHours;
    /**
     * Количество часов лабораторных работ по дисциплине в данном семестре
     */
    private Integer labsHours;
    /**
     * Количество часов практических занятий, семинаров по дисциплине в данном семестре
     */
    private Integer practicesHours;
    /**
     * Общий объем аудиторных часов (РАССЧИТЫВАЕМОЕ ПОЛЕ)
     */
//    private Integer totalHoursWithTeacher;
    /**
     * Объем СРС (РАССЧИТЫВАЕМОЕ ПОЛЕ)
     */
//    private Integer studentIndependentWorkHours;
    /**
     * Объем аудиторных часов в неделю (РАССЧИТЫВАЕМОЕ ПОЛЕ)
     */
//    private Double hoursWithTeacherPerWeek;

    public String getDisciplineNumber() {
        return this.disciplineNumber;
    }

    public void setDisciplineNumber(String disciplineNumber) {
        this.disciplineNumber = disciplineNumber;
    }

    @JoinColumn(nullable = false)
    @ManyToOne
    @NonVisual
    public EduPlanDiscipline getEduPlanDiscipline() {
        return eduPlanDiscipline;
    }

    public void setEduPlanDiscipline(EduPlanDiscipline eduPlanDiscipline) {
        this.eduPlanDiscipline = eduPlanDiscipline;
    }

    @Transient
    public Discipline getDiscipline() {
        try {
            return this.eduPlanDiscipline.getDiscipline();
        } catch (NullPointerException e) {
            return null;
        }
    }

//    public void setDiscipline(Discipline discipline){
//        this.eduPlanDiscipline.setDiscipline(discipline);
//    }

    @JoinColumn(nullable = false)
    @ManyToOne
    public EduPlanSemester getEduPlanSemester() {
        return eduPlanSemester;
    }

    public void setEduPlanSemester(EduPlanSemester eduPlanSemester) {
        this.eduPlanSemester = eduPlanSemester;
    }
    

    public Boolean getIsExam() {
        return this.isExam;
    }

    public void setIsExam(Boolean isExam) {
        this.isExam = isExam;
    }

    public Boolean getIsCredit() {
        return this.isCredit;
    }

    public void setIsCredit(Boolean isCredit) {
        this.isCredit = isCredit;
    }

    public Boolean getIsCourseWork() {
        return this.isCourseWork;
    }

    public void setIsCourseWork(Boolean isCourseWork) {
        this.isCourseWork = isCourseWork;
    }

    public Boolean getIsControlWork() {
        return this.isControlWork;
    }

    public void setIsControlWork(Boolean isControlWork) {
        this.isControlWork = isControlWork;
    }

    public Integer getDisciplineTotalHoursPerRecord() {
        return this.disciplineTotalHoursPerRecord;
    }

    public void setDisciplineTotalHoursPerRecord(Integer disciplineTotalHoursPerRecord) {
        this.disciplineTotalHoursPerRecord = disciplineTotalHoursPerRecord;
    }
//     @Transient
//    public Double getEctsCreditAmount() {
//        return this.disciplineTotalHoursPerRecord/ NormativeValue.ECTSCREDIT;
//    }
//
//    public void setEctsCreditAmount(Double ectsCreditAmount) {
//        this.ectsCreditAmount =ectsCreditAmount;
//    }

    public Integer getLecturesHours() {
        return this.lecturesHours;
    }

    public void setLecturesHours(Integer lecturesHours) {
        this.lecturesHours = lecturesHours;
    }

    public Integer getLabsHours() {
        return this.labsHours;
    }

    public void setLabsHours(Integer labsHours) {
        this.labsHours = labsHours;
    }

    public Integer getPracticesHours() {
        return this.practicesHours;
    }

    public void setPracticesHours(Integer practicesHours) {
        this.practicesHours = practicesHours;
    }
//    @Transient
//    public Integer getTotalHoursWithTeacher() {
//        return this.getLecturesHours() + this.getLabsHours() + this.getPracticesHours();
//    }
//
//    public void setTotalHoursWithTeacher() {
//        this.totalHoursWithTeacher=this.getLecturesHours() + this.getLabsHours() + this.getPracticesHours();
//    }
//     @Transient
//    public Integer getStudentIndependentWorkHours() {
//        return this.disciplineTotalHoursPerRecord - this.getTotalHoursWithTeacher();
//    }
//
//    public void setStudentIndependentWorkHours() {
//        this.studentIndependentWorkHours=this.disciplineTotalHoursPerRecord - this.getTotalHoursWithTeacher();
//    }
//    @Transient
//    public Double getHoursWithTeacherPerWeek() {
//        return Double.valueOf(this.getTotalHoursWithTeacher()/this.eduPlanSemester.getEduWeekAmount());
//    }
//    public void setHoursWithTeacherPerWeek() {
//        this.hoursWithTeacherPerWeek=Double.valueOf(this.getTotalHoursWithTeacher()/this.eduPlanSemester.getEduWeekAmount());
//    }

    @Override
    protected boolean entityEquals(EduPlanRecord obj) {
        return aEqualsField(eduPlanDiscipline, obj.eduPlanDiscipline) && aEqualsField(eduPlanSemester, obj.eduPlanSemester);
    }

    @Override
    public int compareTo(EduPlanRecord o) {
        return o.toString().compareTo(o.toString());
    }
}
