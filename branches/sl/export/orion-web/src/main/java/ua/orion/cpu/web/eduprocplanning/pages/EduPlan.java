package ua.orion.cpu.web.eduprocplanning.pages;

import java.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDiscipline;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanCycle;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanState;
import ua.orion.cpu.core.eduprocplanning.services.EduProcPlanningService;
import ua.orion.web.services.RequestInfo;
import ua.orion.web.services.TapestryDataSource;

/**
 *
 * @author sl
 */
@SuppressWarnings("unused")
public class EduPlan {

    //---Services---
    @Inject
    private RequestInfo info;
    @Inject
    private Logger LOG;
    @Inject
    private TapestryDataSource dataSource;
    @Inject
    private EntityService es;
    @Inject
    private AlertManager alertManager;
    @Inject
    private EduProcPlanningService eduProcPlanningService;
    //---Locals---
    @Component
    private Zone headerZone;
    @Property
    @Persist
    private ua.orion.cpu.core.eduprocplanning.entities.EduPlan object;
    
    @Property
    private EduPlanDiscipline discipline;
    @Property
    private EduPlanCycle cycle;

    public List getCycles() {
        return eduProcPlanningService.findEduPlanCyclesByEduPlan(object);
    }
    
    public List getDisciplines() {
        return eduProcPlanningService.findDisciplinesByEduPlanCycleAndEduPlan(cycle, object);
    }

    public Object onActivate(EventContext context) {
        if (info.isComponentEventRequest()) {
            //Если это событие компонента, то Persistent object
            //должен быть уже установлен
            if (object == null) {
                LOG.debug("Component event on uninitialized page");
                return "";
            }
        } else {
            //Иначе это запрос страницы. Страница должна обязательно получить 
            //верный id сущности, с которой предстоит работа
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                object = context.get(ua.orion.cpu.core.eduprocplanning.entities.EduPlan.class, 0);
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
        }
        return null;
    }

    public Object onSuccessFromApprove() {
        if (EduPlanState.ACTUAL != object.getEduPlanState()) {
            object.setEduPlanState(EduPlanState.ACTUAL);
            object = es.merge(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Учебный план " + es.getStringValue(object) + " утвержден");
            return headerZone.getBody();
        }
        return true;
    }

    public Object onDisable() {
        if (EduPlanState.OBSOLETE != object.getEduPlanState()) {
            object.setEduPlanState(EduPlanState.OBSOLETE);
            object = es.merge(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Учебный план " + es.getStringValue(object) + " упразднен");
            return headerZone.getBody();
        }
        return true;
    }

    public Object onProject() {
        if (EduPlanState.PROJECT != object.getEduPlanState()) {
            object.setEduPlanState(EduPlanState.PROJECT);
            object = es.merge(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Учебный план " + es.getStringValue(object) + " сделан проектом");
            return headerZone.getBody();
        }
        return true;
    }

    public boolean getIsProject() {
        return object.getEduPlanState() == EduPlanState.PROJECT;
    }
    public boolean getIsActual() {
        return object.getEduPlanState() == EduPlanState.ACTUAL;
    }
}
