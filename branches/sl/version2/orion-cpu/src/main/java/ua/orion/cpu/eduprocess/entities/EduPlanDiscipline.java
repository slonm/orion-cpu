package ua.orion.cpu.eduprocess.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;
import ua.orion.core.utils.Defense;

/**
 * Сущность-справочник названий дисциплин
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlanDiscipline extends AbstractEntity<EduPlanDiscipline> {

    private static final long serialVersionUID = 1L;
    /**
     * Название дисциплины
     */
    private Discipline discipline;
    /**
     * Цикл дисциплин для определенного учебного плана, к которому принадлежит данная дисциплина
     */
    private EduPlanDisciplineCycle eduPlanDisciplineCycle;
    /**
     * Общий объем часов дисциплины
     */
    private Integer disciplineTotalHoursVolume;
    /**
     * Является дисциплина обязательной или выборочной
     */
    private Boolean isMandatory;
//    /**
//     * Набор семестров, в которых изучается данная дисциплина
//     */
//    private Set  eduPlanSemesterSet=new HashSet();

    @JoinColumn(nullable = false)
    @ManyToOne
    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = Defense.notNull(discipline, "discipline");
    }

    @JoinColumn(nullable = false)
    @ManyToOne
    public EduPlanDisciplineCycle getEduPlanDisciplineCycle() {
        return eduPlanDisciplineCycle;
    }

    public void setEduPlanDisciplineCycle(EduPlanDisciplineCycle eduPlanDisciplineCycle) {
        this.eduPlanDisciplineCycle = eduPlanDisciplineCycle;
    }

    public Integer getDisciplineTotalHourVolume() {
        return disciplineTotalHoursVolume;
    }

    public void setDisciplineTotalHourVolume(Integer disciplineTotalHoursVolume) {
        this.disciplineTotalHoursVolume = disciplineTotalHoursVolume;
    }

    /**
     * @return the isMandatory
     */
    public Boolean getIsMandatory() {
        return isMandatory;
    }

    /**
     * @param isMandatory the isMandatory to set
     */
    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

//    public Set getEduPlanSemesterSet() {
//        return eduPlanSemesterSet;
//    }
//
//    public void setEduPlanSemesterSet(Set eduPlanSemesterSet) {
//        this.eduPlanSemesterSet = eduPlanSemesterSet;
//    }
//    /**
//     * Связь с семестрами в которых читается данная дисциплина
//     * @return the eduPlanSemesterSet
//     */
//    @OneToMany(mappedBy = "eduPlanDiscipline")
//    public Set<EduPlanSemester> getEduPlanSemesterSet() {
//        return eduPlanSemesterSet;
//    }
//
//    /**
//     * @param eduPlanSemesterSet the eduPlanSemesterSet to set
//     */
//    public void setEduPlanSemesterSet(Set<EduPlanSemester> eduPlanSemesterSet) {
//        this.eduPlanSemesterSet = eduPlanSemesterSet;
//    }
    //TODO Разобраться с методом, получающим набор семестров, в которых читается данная дисциплина
//   /**
//     * Получение набора семестров, в которых читается данная дисциплина
//     * @return the value of eduPlanSemesterSet
//     */
//   public Set<EduPlanSemester> getEduPlanSemesterSet() {
//        for(EduPlanRecord eduPlanRecord)
//
//        for (Iterator<EduPlanSemester> iter = eduPlanSemesterSet.iterator(); iter.hasNext();) {
//            iter.next();
//        }
//        return eduPlanSemesterSet;
//    }
    @Override
    public String toString() {
        String name = "<NULL>";
        String eduPlan = "<NULL>";
        try {
            name = discipline.getName();
        } catch (NullPointerException e) {
        }
        try {
            eduPlan = eduPlanDisciplineCycle.getEduplan().toString();
        } catch (NullPointerException e) {
        }
        return name + " " + eduPlan;
    }

    @Override
    protected boolean entityEquals(EduPlanDiscipline obj) {
        return aEqualsField(discipline, obj.discipline) && aEqualsField(eduPlanDisciplineCycle.getEduplan(), obj.eduPlanDisciplineCycle.getEduplan());
    }

    @Override
    public int compareTo(EduPlanDiscipline o) {
        return o.toString().compareTo(o.toString());
    }
}
