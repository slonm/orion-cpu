package ua.orion.web.pages;

import java.awt.Toolkit;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
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
import ua.orion.web.services.LastPageHolder;
import ua.orion.web.services.TapestryDataSource;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * @author slobodyanuk
 */
@Import(library = "../WindowUtils.js",
stylesheet = "../css/tapestry-crud.css")
@SuppressWarnings("unused")
public class Crud {
    //---Components and component's resources---

    @Component
    private Zone listZone;
    @Inject
    private Block editBlock;
    @Inject
    private Block viewBlock;
    @Inject
    private Block deleteBlock;
    @Inject
    @Property(write = false)
    private Messages messages;
    @Inject
    private ComponentResources resources;
    //---Services---
    @Inject
    private Request request;
    @Inject
    private LastPageHolder lastPageHolder;
    @Inject
    private EntityService entityService;
    @Inject
    private Logger LOG;
    @Inject
    @Property(write = false)
    private TapestryDataSource dataSource;
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
//    @Property(write = false)
//    private String title;
    @Property(write = false)
    private String error;
    private static final String EDIT = "edit";
    private static final String ADD = "add";
    private static final String VIEW = "view";
    private static final String DEL = "del";
    /**
     * Высота формы редактирования
     */
    private Integer formHeightEdit;
    /**
     * Высота формы просмотра
     */
    private Integer formHeightView;
    /**
     * Ширина формы 
     */
    private Integer formWidth;

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
//    public void onPassivate() {
//        if (getObject() != null) {
//            return getActivationContextEncoder(getEntityClass()).
//                    toActivationContext(getObject());
//        } else if (isInitiated()) {
//            return BaseEntity.getFullClassName(getEntityClass());
//        }
//        return null;
//    }
    public Object onActivate(EventContext context) {
        if (!isComponentEventRequst()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                Class<? extends IEntity> objClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();
                //Если страница вызвана для класса сущности, отличного от предыдущего
                //вызова, то сбросим все Persistent поля и отправим редирект на эту же страницу,
                //т.к. сброс произойдеи только при следующем запросе страницы
                if (!objClass.equals(objectClass) && objectClass != null) {
                    resources.discardPersistentFieldChanges();
                    return lastPageHolder.getLastPage();
                }
                objectClass = objClass;
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return startPageName;
            }
        }
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":read");
        return null;
    }

    public GridDataSource getObjects() {
        return dataSource.getGridDataSource(objectClass);
    }

    public Object onSuccessFromEditForm() {
        error = null;
        try {
            entityService.merge(object);
        } catch (RuntimeException ex) {
            error = messages.get("message.update-error");
        }
        return listZone.getBody();
    }

    public Object onSuccessFromAddForm() {
        error = null;
        try {
            entityService.persist(object);
        } catch (RuntimeException ex) {
            error = messages.get("message.insert-error");
        }
        return listZone.getBody();
    }

    /**
     * Метод расчета ширины формы редактирования. 
     * Исходит из того, что ширина должна составлять 90% от ширины экрана(разрешения)
     * @return значение ширины формы (в пикселях)
     */
    public Integer getFormWidth() {
        return (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7);
    }

    public Object onEdit(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":update:" + id);
        mode = EDIT;
        object = entityService.find(objectClass, id);
        error = null;
        return editBlock;
    }

    public Object onAdd() {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":insert");
        mode = ADD;
        object = entityService.newInstance(objectClass);
        error = null;
        return editBlock;
    }

    public Object onView(Integer id) {
        mode = VIEW;
        object = entityService.find(objectClass, id);
        error = null;
        return viewBlock;
    }

    public Object onTryDelete(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":delete:" + id);
        mode = DEL;
        object = entityService.find(objectClass, id);
        error = null;
        return deleteBlock;
    }

    public Object onDelete(Integer id) {
        error = null;
        try {
            object = entityService.find(objectClass, id);
            entityService.remove(object);
        } catch (RuntimeException ex) {
            error = messages.get("message.delete-error");
        }
        return listZone.getBody();
    }

    public boolean getIsEdit() {
        return EDIT.equals(mode);
    }
    
    /**
     * Переопределение этого метода в наследниках позволит кастомизировать титул страниц
     * @return 
     */
    public String getTitle(){
        return messages.get("entity." + objectClass.getSimpleName());
    }
}
