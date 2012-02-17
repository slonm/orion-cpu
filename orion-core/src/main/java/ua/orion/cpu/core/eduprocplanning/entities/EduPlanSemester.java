package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;
import ua.orion.core.persistence.AbstractNamedEntity;

/**
 * сущность семестр в учебном плане
 * ПОКА НЕ ИСПОЛЬЗУЕМ БУДЕТ ИСПОЛЬЗОВАНА В ПОДСИСТЕМЕ УЧЕБНОГО И РАБОЧЕГО ВРЕМЕНИ
 * @author kgp
 */
@Entity
public class EduPlanSemester extends AbstractNamedEntity<EduPlanSemester> {

    private static final long serialVersionUID = 1L;

    //Количество недель теоретических занятий в данном семестре
    private Integer eduWeekAmount;

    public EduPlanSemester() {
    }

    public EduPlanSemester(String name, Integer eduWeekAmount) {
        this.setName(null);
        this.eduWeekAmount = eduWeekAmount;
    }
    
    public Integer getEduWeekAmount() {
        return eduWeekAmount;
    }

    public void setEduWeekAmount(Integer eduWeekAmount) {
        this.eduWeekAmount = eduWeekAmount;
    }
    
//    //Двунаправленная ассоциация с дисциплинами учебного плана, читаемыми в данном семестре
//    @ManyToOne
//    public EduPlanDiscipline getEduPlanDiscipline() {
//        return eduPlanDiscipline;
//    }
//
//    public void setEduPlanDiscipline(EduPlanDiscipline eduPlanDiscipline) {
//        this.eduPlanDiscipline = eduPlanDiscipline;
//    }
}
