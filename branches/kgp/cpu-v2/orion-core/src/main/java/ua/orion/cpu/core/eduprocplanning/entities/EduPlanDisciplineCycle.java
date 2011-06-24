/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.eduprocplanning.entities;

import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.*;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Цикл дисциплин в привязке к конкретномуучебному плану
 * @author kgp
 */
@Entity
@Table(schema = "uch")
public class EduPlanDisciplineCycle extends AbstractEntity<EduPlanDisciplineCycle> {

    private static final long serialVersionUID = 1L;
    
    //Поле связи с учебным планом
    private EduPlan eduPlan;
    //Название цикла дисциплин (берётся из справочника)
    private EPPCycle ePPCycle;
    //Порядковый номер цикла дисциплин (берется из ОПП)
    private String eduPlanDisciplineCycleNumber;
    //Явлется ли цикл дисциплин учебного плана нормативным
    private Boolean isRegulatory;
    //TODO НА ФОРМЕ ВВОДА КОЛИЧЕСТВА КРЕДИТОВ ОГРАНИЧИТЬ ЗНАЧЕНИЯ 
    //ТОЛЬКО С ДРОБНОЙ ЧАСТЬЮ .0 ИЛИ .25  ИЛИ .5 ИЛИ .75 
    //Общее количество кредитов на дисциплины цикла
    private Double cycleTotalCredits;
    //Дисциплины цикла учебного плана
    private SortedSet<EduPlanDiscipline> eduPlanCycleDisciplines = new TreeSet<EduPlanDiscipline>();

    public EduPlanDisciplineCycle() {
    }

    public EduPlanDisciplineCycle(EduPlan eduPlan, EPPCycle ePPCycle, String eduPlanDisciplineCycleNumber, 
            Boolean isRegulatory, Double cycleTotalCredits) {
        this.eduPlan = eduPlan;
        this.ePPCycle = ePPCycle;
        this.eduPlanDisciplineCycleNumber = eduPlanDisciplineCycleNumber;
        this.isRegulatory = isRegulatory;
        this.cycleTotalCredits = cycleTotalCredits;
    }

    //Двунаправленная ассоциация с учебным планом
    @ManyToOne
    public EduPlan getEduPlan() {
        return eduPlan;
    }

    public void setEduPlan(EduPlan eduPlan) {
        this.eduPlan = eduPlan;
    }
    
    //Однонаправленная ассоциация со справочником циклов освітньо-професійних програм
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

    public Boolean getIsRegulatory() {
        return isRegulatory;
    }

    public void setIsRegulatory(Boolean isRegulatory) {
        this.isRegulatory = isRegulatory;
    }

    public Double getePPCycleTotalCredits() {
        return cycleTotalCredits;
    }

    public void setePPCycleTotalCredits(Double ePPCycleTotalCredits) {
        this.cycleTotalCredits = ePPCycleTotalCredits;
    }

     /**
     * Геттер для отображения общего количества часов цикла дисциплины в гриде 
     * (сеттер для вычислимого поля, как и само поле, не нужны)
     */
    @Transient
    public Double getCycleTotalHours() {
        return cycleTotalCredits*NormativeValue.ECTSCREDIT;
    }

    //Двунаправленная ассоциация с дисциплинами учебного плана, входящими в данный цикл
    @Sort(type = SortType.NATURAL)
    @OneToMany(mappedBy = "eduPlanDisciplineCycle")
    public SortedSet<EduPlanDiscipline> getEduPlanCycleDisciplines() {
        return eduPlanCycleDisciplines;
    }

    public void setEduPlanCycleDisciplines(SortedSet<EduPlanDiscipline> eduPlanCycleDisciplines) {
        this.eduPlanCycleDisciplines = eduPlanCycleDisciplines;
    }

    @Override
    public String toString() {
        try {
            return this.ePPCycle.getName();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    protected boolean entityEquals(EduPlanDisciplineCycle obj) {
        return aEqualsField(ePPCycle, obj.ePPCycle);
    }

    @Override
    public int compareTo(EduPlanDisciplineCycle o) {
        return o.toString().compareTo(o.toString());
    }
}
