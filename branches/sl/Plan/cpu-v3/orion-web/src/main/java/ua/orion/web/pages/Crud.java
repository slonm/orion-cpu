package ua.orion.web.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.persistence.MetaEntity;
import ua.orion.web.services.RequestInfo;

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
    private RequestInfo info;
    @Inject
    private Logger LOG;
    //---Locals---
    @Persist
    private Class<? extends IEntity> objectClass;

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
        if (info.isComponentEventRequest()) {
            //Если это событие компонента, то Persistent objectClass
            //должен быть уже установлен
            if (objectClass == null) {
                LOG.debug("Component event on uninitialized page");
                return "";
            }
        } else {
            //Иначе это запрос страницы. Страница должна обязательно получить класс
            //сущности
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                Class<? extends IEntity> objClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();
                //Если страница вызвана для класса сущности, отличного от предыдущего
                //вызова, то сбросим все Persistent поля и отправим редирект на эту же страницу,
                //т.к. сброс произойдет только при следующем запросе страницы
                if (!objClass.equals(objectClass) && objectClass != null) {
                    resources.discardPersistentFieldChanges();
                    return info.getLastPage();
                }
                objectClass = objClass;
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
        }
        return null;
    }
}
