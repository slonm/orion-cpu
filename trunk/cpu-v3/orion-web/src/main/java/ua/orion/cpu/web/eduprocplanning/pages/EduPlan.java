package ua.orion.cpu.web.eduprocplanning.pages;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDiscipline;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanState;
import ua.orion.web.AdditionalConstraintsApplier;
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
    //---Locals---
    @Property
    @Persist
    private ua.orion.cpu.core.eduprocplanning.entities.EduPlan object;

    public Class<?> getDisciplineClass() {
        return EduPlanDiscipline.class;
    }

    public GridDataSource getSource() {
        return dataSource.getGridDataSource(EduPlanDiscipline.class, new AdditionalConstraintsApplier<EduPlanDiscipline>() {

            @Override
            public void applyAdditionalConstraints(CriteriaQuery<EduPlanDiscipline> criteria, Root<EduPlanDiscipline> root, CriteriaBuilder builder) {
                criteria.where(builder.equal(root.get("eduPlan"), object));
            }
        });
    }

    public Object onPassivate() {
        return object;
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
                object.getId(); //throw exception if object is null
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            SecurityUtils.getSubject().checkPermission("EduPlan:read:" + object.getId());
        }
        return null;
    }

    public void onSuccessFromApprove() {
        object.setEduPlanState(EduPlanState.ACTUAL);
        es.getEntityManager().merge(object);
    }

    public void onDisable() {
        object.setEduPlanState(EduPlanState.OBSOLETE);
        es.getEntityManager().merge(object);
    }

    public void onProject() {
        object.setEduPlanState(EduPlanState.PROJECT);
        es.getEntityManager().merge(object);
    }

    public boolean getIsProject() {
        return object.getEduPlanState() == EduPlanState.PROJECT;
    }
}
