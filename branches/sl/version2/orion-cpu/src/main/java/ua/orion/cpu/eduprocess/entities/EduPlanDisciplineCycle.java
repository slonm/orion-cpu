package ua.orion.cpu.eduprocess.entities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Цикл дисциплин в привязке к конкретномуучебному плану
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlanDisciplineCycle extends AbstractEntity<EduPlanDisciplineCycle> {

    private static final long serialVersionUID = 1L;
    /**
     * Название цикла дисциплин
     */
    private EPPCycle ePPCycle;
    /**
     * Номер цикла дисциплин (берется из ОПП)
     */
    private String eduPlanDisciplineCycleNumber;
    /**
     * Учебный план, которому принадлежит цикл дисциплин
     */
    private EduPlan eduPlan;
    /**
     * Является ли цикл дисциплин нормативным
     */
    private Boolean isRegulatory;
    /**
     * Общее количество часов дисциплин цикла
     */
    private Integer cycleTotalHours;

    /**
     * Связь с сущностью названий циклов освітньо-професійних програм
     * @return название цикла освітньо-професійних програм
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public EPPCycle getEPPCycle() {
        return ePPCycle;
    }

    public void setEPPCycle(EPPCycle ePPCycle) {
        this.ePPCycle = ePPCycle;
    }

    public String getEduPlanDisciplineCycleNumber() {
        return eduPlanDisciplineCycleNumber;
    }

    public void setEduPlanDisciplineCycleNumber(String eduPlanDisciplineCycleNumber) {
        this.eduPlanDisciplineCycleNumber = eduPlanDisciplineCycleNumber;
    }

    /**
     * Связь циклв дисциплин с учебным планом
     * @return конкретный учебный план
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    public EduPlan getEduplan() {
        return eduPlan;
    }

    public void setEduplan(EduPlan eduPlan) {
        this.eduPlan = eduPlan;
    }

    /**
     * Явлется ли цикл дисциплин учебного плана нормативным
     * @return the value of isRegulatory
     */
    public Boolean getIsRegulatory() {
        return isRegulatory;
    }

    public void setIsRegulatory(Boolean isRegulatory) {
        this.isRegulatory = isRegulatory;
    }

    public Integer getePPCycleTotalHours() {
        return cycleTotalHours;
    }

    public void setePPCycleTotalHours(Integer ePPCycleTotalHours) {
        this.cycleTotalHours = ePPCycleTotalHours;
    }

    @Override
    public String toString(){
        try{
        return this.ePPCycle.getName();
        }catch(NullPointerException e){
            return null;
        }
    }

    @Override
    protected boolean entityEquals(EduPlanDisciplineCycle obj) {
        return aEqualsField(ePPCycle, obj.ePPCycle) && aEqualsField(eduPlan, obj.eduPlan);
    }

    @Override
    public int compareTo(EduPlanDisciplineCycle o) {
        return o.toString().compareTo(o.toString());
    }
}
