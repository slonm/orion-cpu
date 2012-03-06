package ua.orion.cpu.core.eduprocplanning.services;

import java.util.*;
import ua.orion.cpu.core.eduprocplanning.entities.*;
import ua.orion.cpu.core.licensing.entities.*;

/**
 *
 * @author sl
 */
public interface EduProcPlanningService {

    List<EduPlanCycle> findEduPlanCyclesByEduPlan(EduPlan plan);

    List<EduPlanDiscipline> findDisciplinesByEduPlanCycleAndEduPlan(EduPlanCycle cycle, EduPlan plan);
}
