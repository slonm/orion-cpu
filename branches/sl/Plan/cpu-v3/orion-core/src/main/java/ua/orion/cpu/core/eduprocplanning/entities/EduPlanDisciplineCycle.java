package ua.orion.cpu.core.eduprocplanning.entities;

import java.util.*;
import javax.persistence.*;

/**
 * Цикл дисциплин в привязке к конкретному учебному плану
 * @author kgp
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"eduPlan", "EPPCycle"}))
public class EduPlanDisciplineCycle extends EduPlanDisciplineTag<EduPlanDisciplineCycle> {

    private static final long serialVersionUID = 1L;
    private EduPlan eduPlan;
    //Название цикла дисциплин (берётся из справочника)
    private EPPCycle ePPCycle;
    //Порядковый номер цикла дисциплин (берется из ОПП)
    private String number;
    //Явлется ли цикл дисциплин учебного плана нормативным
    private Boolean isRegulatory;
    //TODO НА ФОРМЕ ВВОДА КОЛИЧЕСТВА КРЕДИТОВ ОГРАНИЧИТЬ ЗНАЧЕНИЯ 
    //ТОЛЬКО С ДРОБНОЙ ЧАСТЬЮ .0 ИЛИ .25  ИЛИ .5 ИЛИ .75 
    //Общее количество кредитов на дисциплины цикла
    private Double totalCredits;
    //Части циклов
    private Set<EduPlanSubCycle> eduPlanSubCycles = new HashSet<>();

    public EduPlanDisciplineCycle() {
    }

    public EduPlanDisciplineCycle(EduPlan eduPlan, EPPCycle ePPCycle, String number,
            Boolean isRegulatory, Double totalCredits) {
        this.eduPlan = eduPlan;
        this.ePPCycle = ePPCycle;
        this.number = number;
        this.isRegulatory = isRegulatory;
        this.totalCredits = totalCredits;
    }

    //Связь с учебным планом
    @ManyToOne
    @JoinColumn(name="eduPlan")
    public EduPlan getEduPlan() {
        return eduPlan;
    }

    public void setEduPlan(EduPlan eduPlan) {
        this.eduPlan = eduPlan;
    }

    //Однонаправленная ассоциация со справочником циклов освітньо-професійних програм
    @JoinColumn(nullable = false, name="EPPCycle")
    @ManyToOne
    public EPPCycle getEPPCycle() {
        return ePPCycle;
    }

    public void setEPPCycle(EPPCycle ePPCycle) {
        this.ePPCycle = ePPCycle;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getIsRegulatory() {
        return isRegulatory;
    }

    public void setIsRegulatory(Boolean isRegulatory) {
        this.isRegulatory = isRegulatory;
    }

    public Double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Double totalCredits) {
        this.totalCredits = totalCredits;
    }

    @OneToMany(mappedBy="eduPlanCycle")
    public Set<EduPlanSubCycle> getEduPlanSubCycles() {
        return eduPlanSubCycles;
    }

    public void setEduPlanSubCycles(Set<EduPlanSubCycle> eduPlanSubCycles) {
        this.eduPlanSubCycles = eduPlanSubCycles;
    }
    
    /**
     * Геттер для отображения общего количества часов цикла дисциплины в гриде 
     * (сеттер для вычислимого поля, как и само поле, не нужны)
     */
    @Transient
    public Double getTotalHours() {
        return totalCredits * NormativeValue.ECTSCREDIT;
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
        return number.compareTo(o.number);
    }
}
