package ua.orion.cpu.web.eduprocplanning.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanCycle;
import ua.orion.web.pages.Crud;
import ua.orion.web.services.RequestInfo;
import ua.orion.web.services.TapestryDataSource;

/**
 *
 * @author kgp
 */
public class EduPlanDisciplineCycleWeb extends Crud {

    @Inject
    private ComponentResources resources;
    @Inject
    private RequestInfo info;
    @Inject
    @Property(write = false)
    private TapestryDataSource dataSource;
    @Inject
    private EntityService entityService;
    @Inject
    @Property(write = false)
    private Messages messages;
    @Inject
    private Logger LOG;
    @Inject
    @Symbol(SymbolConstants.START_PAGE_NAME)
    private String startPageName;
//    @Persist
//    private Class<? extends IEntity> objectClass;
//    @Persist
//    private IEntity object;
    @Property(write = false)
    private String error;
    @Property
    private EduPlanCycle eduPlanDisciplineCycle;
    private EduPlan eduPlan;

    @Override
    public Object onActivate(EventContext context) {
        if (!info.isComponentEventRequest()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                //eduPlanDisciplineCycle = new EduPlanDisciplineCycle();
                //            Class<? extends IEntity> objClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();
                
                String id = context.get(String.class, 0);
                eduPlanDisciplineCycle=entityService.find(EduPlanCycle.class, id);

                Class<? extends IEntity> objClass = eduPlanDisciplineCycle.getClass();
                //Если страница вызвана для класса сущности, отличного от предыдущего
                //вызова, то сбросим все Persistent поля и отправим редирект на эту же страницу,
                //т.к. сброс произойдеи только при следующем запросе страницы
                if (!objClass.equals(getObjectClass()) && getObjectClass() != null) {
                    resources.discardPersistentFieldChanges();
                    return info.getLastPage();
                }
                setObjectClass(objClass);

//                
//super.getObject();
//                entityService.find(getObjectClass(), id);
//                eduPlan = ((EduPlanDisciplineCycle) getObject()).getEduPlan();
//                error = null;
//                return object;

            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return startPageName;
            }
        }
        SecurityUtils.getSubject().checkPermission(getObjectClass().getSimpleName() + ":read");
        return null;
    }

//    @Override
//    public GridDataSource getObjects() {
//        return super.getObjects();
//    }
//    @Override
//    public Class<?> getObjectClass() {
//        return super.getObjectClass();
//    }
//
//    @Override
//    public void setObjectClass(Class<? extends IEntity> objectClass) {
//        super.setObjectClass(objectClass);
//    }

    @Override
    public String getTitle() {
        return eduPlan + " - " + messages.get("entity." + getObjectClass().getSimpleName());
    }
}
