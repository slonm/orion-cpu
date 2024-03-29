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
import org.slf4j.Logger;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanState;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.web.AdditionalConstraintsApplier;
import ua.orion.web.services.RequestInfo;
import ua.orion.web.services.TapestryDataSource;
/**
 *
 * @author sl
 */
@SuppressWarnings("unused")
public class EduPlans {

    //---Services---
    @Inject
    private RequestInfo info;
    @Inject
    private Logger LOG;
    @Property
    @Persist
    private EduPlanState eduPlanState;
    @Inject
    private TapestryDataSource dataSource;
    //Состояния, соответствующие закладкам
    private static final EduPlanState[] states =
            new EduPlanState[]{EduPlanState.ACTUAL, EduPlanState.PROJECT, EduPlanState.OBSOLETE, null};

    /**
     * Получение DataSource для Grid
     * @return GridDataSource
     */
    public GridDataSource getSource() {
        return dataSource.getGridDataSource(EduPlan.class, new AdditionalConstraintsApplier<EduPlan>(){

            /**
             * Ограничение получамых записей по полю eduPlanState.
             * Вкладки - Действующие,  Разрабатываемые,  Упразднённые,  Все.
             */
            @Override
            public void applyAdditionalConstraints(CriteriaQuery<EduPlan> criteria, Root<EduPlan> root, CriteriaBuilder builder) {
                if (eduPlanState != null) {
                    criteria.where(builder.equal(root.get("eduPlanState"), eduPlanState));
                }
            }
        });
    }

    public Object onActivate(EventContext context) {
        if (!info.isComponentEventRequest()) {
            //Страница не должна получать контекста при запросе страницы
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

    public void onTabSelect(int tab) {
        eduPlanState = states[tab];
    }
}
