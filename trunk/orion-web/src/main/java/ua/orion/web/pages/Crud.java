package ua.orion.web.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
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
    private String entityType;

    public String getTitle() {
        return messages.get("entity." + entityType);
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public void setEntityType(String objectType) {
        this.entityType = objectType;
    }

    public Object onActivate(EventContext context) {
        if (info.isComponentEventRequest()) {
            //Если это событие компонента, то Persistent objectClass
            //должен быть уже установлен
            if (entityType == null) {
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
                context.get(MetaEntity.class, 0);//Проверка того, что это сущность
                String objType = context.get(String.class, 0);
                //Если страница вызвана для класса сущности, отличного от предыдущего
                //вызова, то сбросим все Persistent поля и отправим редирект на эту же страницу,
                //т.к. сброс произойдет только при следующем запросе страницы
                if (!objType.equals(entityType) && entityType != null) {
                    resources.discardPersistentFieldChanges();
                    return info.getLastPage();
                }
                entityType = objType;
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
        }
        return null;
    }
}
