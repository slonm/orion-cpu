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
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDiscipline;

/**
 *
 * @author sl
 */
@SuppressWarnings("unused")
public class EduPlan {

    //---Services---
    @Inject
    private Request request;
    @Inject
    private Logger LOG;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    @Property(write = false)
    private EntityService entityService;
    //---Locals---
    @Property
    @Persist
    private ua.orion.cpu.core.eduprocplanning.entities.EduPlan object;

    private boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    public Class<?> getDisciplineClass() {
        return EduPlanDiscipline.class;
    }

    public GridDataSource getSource() {
        return new JpaGridDataSource<EduPlanDiscipline>(entityService.getEntityManager(), EduPlanDiscipline.class) {

            @Override
            protected void applyAdditionalConstraints(CriteriaQuery<?> criteria, Root<EduPlanDiscipline> root, CriteriaBuilder builder) {
                criteria.where(builder.equal(root.get("eduPlan"), object));
            }
        };
    }

    public Object onActivate(EventContext context) {
        if (!isComponentEventRequst()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                object = context.get(ua.orion.cpu.core.eduprocplanning.entities.EduPlan.class, 0);
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            SecurityUtils.getSubject().checkPermission("EduPlan:read:" + object.getId());
        }
        return null;
    }
}