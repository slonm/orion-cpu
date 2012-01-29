package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;

/**
 * Часть цикла дисциплин в привязке к конкретному циклу
 * @author kgp
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"eduPlanDisciplineCycle", "name"}))
public class EduPlanSubCycle extends EduPlanDisciplineTag<EduPlanSubCycle> {

    private static final long serialVersionUID = 1L;
    private EduPlanDisciplineCycle eduPlanDisciplineCycle;
    //Порядковый номер части цикла дисциплин
    private String number;
    //Название части цикла дисциплин
    private EduPlanSubCycleType name;
    //TODO НА ФОРМЕ ВВОДА КОЛИЧЕСТВА КРЕДИТОВ ОГРАНИЧИТЬ ЗНАЧЕНИЯ 
    //ТОЛЬКО С ДРОБНОЙ ЧАСТЬЮ .0 ИЛИ .25  ИЛИ .5 ИЛИ .75 
    //Общее количество кредитов на дисциплины цикла
    private Double totalCredits;
    private boolean isVariant = false;

    public EduPlanSubCycle() {
    }

    public EduPlanSubCycle(EduPlanDisciplineCycle eduPlanDisciplineCycle, 
            String number, EduPlanSubCycleType name, Double cycleTotalCredits) {
        this.eduPlanDisciplineCycle = eduPlanDisciplineCycle;
        this.number = number;
        this.name = name;
        this.totalCredits = cycleTotalCredits;
    }

    //Связь с циклом
    @ManyToOne
    @JoinColumn(name = "eduPlanDisciplineCycle")
    public EduPlanDisciplineCycle getEduPlanDisciplineCycle() {
        return eduPlanDisciplineCycle;
    }

    public void setEduPlanDisciplineCycle(EduPlanDisciplineCycle eduPlanDisciplineCycle) {
        this.eduPlanDisciplineCycle = eduPlanDisciplineCycle;
    }

    public boolean isIsVariant() {
        return isVariant;
    }

    public void setIsVariant(boolean isVariant) {
        this.isVariant = isVariant;
    }

    public EduPlanSubCycleType getName() {
        return name;
    }

    public void setName(EduPlanSubCycleType name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public Double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Double totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * Геттер для отображения общего количества часов части цикла дисциплины в гриде 
     * (сеттер для вычислимого поля, как и само поле, не нужны)
     */
    @Transient
    public Double getTotalHours() {
        return totalCredits * NormativeValue.ECTSCREDIT;
    }

    @Override
    public String toString() {
        try {
            return name.toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    protected boolean entityEquals(EduPlanSubCycle obj) {
        return aEqualsField(eduPlanDisciplineCycle, obj.eduPlanDisciplineCycle)&&
               aEqualsField(name, obj.name) ;
    }

    @Override
    public int compareTo(EduPlanSubCycle o) {
        return number.compareTo(o.number);
    }
}
