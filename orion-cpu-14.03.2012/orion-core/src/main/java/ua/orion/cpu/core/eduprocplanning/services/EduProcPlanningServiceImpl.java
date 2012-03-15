package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import javax.persistence.TypedQuery;
import ua.orion.cpu.core.licensing.entities.License;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.eduprocplanning.entities.*;

/**
 *
 * @author sl
 */
public class EduProcPlanningServiceImpl implements EduProcPlanningService {

    private final EntityService es;

    public EduProcPlanningServiceImpl(EntityService entityService) {
        this.es = entityService;
    }

    @Override
    public List<EduPlanCycle> findEduPlanCyclesByEduPlan(EduPlan plan) {
        String source = "select c from EduPlanCycle c"
                + " where c.eduPlan=:plan order by c.number";
        TypedQuery<EduPlanCycle> query = es.getEntityManager().createQuery(source, EduPlanCycle.class);
        query.setParameter("plan", plan);
        return query.getResultList();
    }
    
    @Override
    public List<EduPlanDiscipline> findDisciplinesByEduPlanCycleAndEduPlan(EduPlanCycle cycle, EduPlan plan) {
        String source = "select d from EduPlanDiscipline d join d.eduPlan"
                + " where d.eduPlan=:plan and :cycle MEMBER OF d.eduPlanDisciplineTags"
                + " order by d.number";
        TypedQuery<EduPlanDiscipline> query = es.getEntityManager().createQuery(source, EduPlanDiscipline.class);
        query.setParameter("plan", plan);
        query.setParameter("cycle", cycle);
        return query.getResultList();
    }
}
