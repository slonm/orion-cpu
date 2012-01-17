package ua.orion.cpu.core.eduprocplanning.entities;

import java.util.*;
import javax.persistence.*;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Тег дисциплины учебного плана
 * @author sl
 */
@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class EduPlanDisciplineTag<T extends EduPlanDisciplineTag<?>> extends AbstractEntity<T> {

    private static final long serialVersionUID = 1L;
    //Дисциплины
    private Set<EduPlanDiscipline> eduPlanDisciplines = new HashSet();

    public EduPlanDisciplineTag() {
    }

    //Двунаправленная ассоциация с дисциплинами
    @ManyToMany(mappedBy = "eduPlanDisciplineTags")
    public Set<EduPlanDiscipline> getEduPlanDisciplines() {
        return eduPlanDisciplines;
    }

    public void setEduPlanDisciplines(Set<EduPlanDiscipline> eduPlanDisciplines) {
        this.eduPlanDisciplines = eduPlanDisciplines;
    }
}
