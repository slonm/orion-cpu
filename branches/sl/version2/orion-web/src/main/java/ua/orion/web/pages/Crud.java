package ua.orion.web.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.services.EntityService;
import ua.orion.core.persistence.MetaEntity;
import ua.orion.web.services.TapestryComponentDataSource;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * @author slobodyanuk
 */
@Import(library = "../WindowUtils.js",
        stylesheet="../css/tapestry-crud.css")
public class Crud {
    //---Components and component's resources---

    @Component
    private Zone listZone;
    @Inject
    private Block editBlock;
    @Inject
    private Block viewBlock;
    @Inject
    @Property(write = false)
    private Messages messages;
    //---Services---
    @Inject
    private Request request;
    @Inject
    private EntityService entityService;
    @Inject
    private Logger LOG;
    @Inject
    @Property(write = false)
    private TapestryComponentDataSource dataSource;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    @Symbol(SymbolConstants.START_PAGE_NAME)
    private String startPageName;
    //---Locals---
    @Persist
    private Class<? extends IEntity> objectClass;
    @Persist
    private IEntity object;
    @Persist
    @Property(write = false)
    private String mode;
    private static final String EDIT = "edit";
    private static final String ADD = "add";
    private static final String VIEW = "view";
    private static final String DEL = "del";

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public IEntity getObject() {
        return object;
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public void setObject(IEntity object) {
        this.object = object;
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public Class<?> getObjectClass() {
        return objectClass;
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public void setObjectClass(Class<? extends IEntity> objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * При закрытии страницы
     * @return
     * @author sl
     */
//    public Object onPassivate() {
//        if (getObject() != null) {
//            return getActivationContextEncoder(getEntityClass()).
//                    toActivationContext(getObject());
//        } else if (isInitiated()) {
//            return BaseEntity.getFullClassName(getEntityClass());
//        }
//        return null;
//    }
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {
        if (!isComponentEventRequst()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                objectClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return startPageName;
            }
        }
//        SecurityUtils.getSubject().isPermitted(objectClass.getSimpleName()+":read");
        //title = messages.get("reflect." + beanClass.getName());
        return null;
    }

    public GridDataSource getObjects() {
        return dataSource.getGridDataSource(objectClass);
    }

    public Object onSuccessFromEditForm() {
        entityService.merge(object);
        return listZone.getBody();
    }

    public Object onSuccessFromAddForm() {
        entityService.persist(object);
        return listZone.getBody();
    }

    public Object onEdit(Integer id) {
        mode = EDIT;
        object = entityService.find(objectClass, id);
        return editBlock;
    }

    public Object onAdd() {
        mode = ADD;
        object = entityService.newInstance(objectClass);
        return editBlock;
    }

    public Object onView(Integer id) {
        mode = VIEW;
        object = entityService.find(objectClass, id);
        return viewBlock;
    }

    public Object onDelete(Integer id) {
        mode = DEL;
        entityService.remove(entityService.find(objectClass, id));
        return listZone.getBody();
    }

    public boolean getIsEdit() {
        return EDIT.equals(mode);
    }
}
