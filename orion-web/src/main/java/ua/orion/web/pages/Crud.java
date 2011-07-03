package ua.orion.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.persistence.MetaEntity;
import ua.orion.web.services.LastPageHolder;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * @author slobodyanuk
 */
@SuppressWarnings("unused")
public class Crud {
    //---Components and component's resources---
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
    private Logger LOG;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    @Symbol(SymbolConstants.START_PAGE_NAME)
    private String startPageName;
    //---Locals---
    @Persist
    private Class<? extends IEntity> objectClass;

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    public String getTitle() {
        return messages.get("entity." + objectClass.getSimpleName());
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
                if(!objClass.equals(objectClass)&&objectClass!=null){
                    resources.discardPersistentFieldChanges();
                    return lastPageHolder.getLastPage();
                }
                objectClass=objClass;
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return startPageName;
            }
        }
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":read");
        return null;
    }
}
