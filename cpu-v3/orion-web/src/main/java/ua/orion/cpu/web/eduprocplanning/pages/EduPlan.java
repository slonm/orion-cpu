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
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDiscipline;
import ua.orion.web.AdditionalConstraintsApplier;
import ua.orion.web.services.TapestryDataSource;

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
    private TapestryDataSource dataSource;
    //---Locals---
    @Property
    @Persist
    private ua.orion.cpu.core.eduprocplanning.entities.EduPlan object;
            
    public Class<?> getDisciplineClass() {
        return EduPlanDiscipline.class;
    }

    public GridDataSource getSource() {
        return dataSource.getGridDataSource(EduPlanDiscipline.class, new AdditionalConstraintsApplier<EduPlanDiscipline>(){

            @Override
            public void applyAdditionalConstraints(CriteriaQuery<EduPlanDiscipline> criteria, Root<EduPlanDiscipline> root, CriteriaBuilder builder) {
                criteria.where(builder.equal(root.get("eduPlan"), object));
            }
        });
    }

    public Object onActivate(EventContext context) {
        if (!request.isXHR()) {
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
}
