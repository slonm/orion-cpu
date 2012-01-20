package ua.orion.cpu.web.eduprocplanning.pages;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanState;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
/**
 *
 * @author sl
 */
@SuppressWarnings("unused")
public class EduPlans {

    @Component
    private Zone crudZone;
    //---Services---
    @Inject
    private Request request;
    @Inject
    private Logger LOG;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    private EntityService entityService;
    @Property
    @Persist
    private EduPlanState eduPlanState;
    //Состояния, соответствующие закладкам
    private static final EduPlanState[] states =
            new EduPlanState[]{EduPlanState.ACTUAL, EduPlanState.PROJECT, EduPlanState.OBSOLETE, null};

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    public Class<?> getObjectClass() {
        return EduPlan.class;
    }

    public GridDataSource getSource() {
        return new JpaGridDataSource<EduPlan>(entityService.getEntityManager(), EduPlan.class) {

            @Override
            protected void applyAdditionalConstraints(CriteriaQuery<?> criteria,
                    Root<EduPlan> root, CriteriaBuilder builder) {
                if (eduPlanState != null) {
                    criteria.where(builder.equal(root.get("eduPlanState"), eduPlanState));
                }
            }
        };
    }

    public Object onActivate(EventContext context) {
        if (!isComponentEventRequst()) {
            try {
                if (context.getCount() != 0) {
                    throw new RuntimeException();
                }
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            eduPlanState = EduPlanState.ACTUAL;
        }
        return null;
    }

    public Object onSelect(int tab) {
        eduPlanState = states[tab];
        return crudZone.getBody();
    }
}
