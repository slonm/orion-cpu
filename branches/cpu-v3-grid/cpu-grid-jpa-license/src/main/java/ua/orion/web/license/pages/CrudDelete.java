package ua.orion.web.license.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
// import org.apache.tapestry5.services.BeanModelSource;
import org.slf4j.Logger;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.persistence.MetaEntity;
import ua.orion.core.services.EntityService;
import ua.orion.web.services.*;

/**
 *
 * @author dobro
 */
public class CrudDelete {

    /**
     * Сообщения интерфейса
     */
    @Inject
    @Property(write = false)
    private Messages messages;
    /**
     * Логгер
     */
    @Inject
    private Logger LOG;
    /**
     * Класс сущности, с которой будем работать
     */
    //@Persist
    private Class<? extends IEntity> entityClass;
    /**
     * Объект сущности, с которой будем работать
     */
    private IEntity entityObject;
    /**
     * Основной сервис для работы с моделью данных. 
     */
    @Inject
    private EntityService entityService;
    /**
     * Константа - путь в дереве меню
     */
    @Persist
    @Property
    private String menupath;
    /**
     * Feedback message
     */
    @Persist
    private String feedbackMessage;
    /**
     * Сервис, составляющий модель (метаданные)
     * необходимые для отображения и обработки формы
     */
    @Inject
    @Property(write = false)
    private TapestryDataSource dataSource;

    /**
     * Creates a {@link BeanModel} and removes the primary key property from it.
     * @return a {@link BeanModel}.
     */
    public BeanModel getBeanModel() {
        // BeanModel beanModel = beanModelSource.createEditModel(this.entityClass, messages);
        BeanModel beanModel = dataSource.getBeanModelForView(entityClass, messages);
        return beanModel;
    }

    public String getFeedbackMessage() {
        String msg = (this.feedbackMessage == null) ? "" : this.feedbackMessage;
        this.feedbackMessage = "";
        return msg;
    }

    public void setFeedbackMessage(String _feedbackMessage) {
        this.feedbackMessage = _feedbackMessage;
    }

    public Object onActivate(EventContext context) {
        // ---------------- имя класса сущности - начало -----------------------
        // имя класса сущности
        Class<? extends IEntity> objClass = null;

        // надо проверить, было ли отправлено имя класса
        if (context.getCount() >= 1) {
            try {
                // имя класса есть, достаем класс по имени
                objClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();
            } catch (java.lang.RuntimeException ex) {
                LOG.error(ex.getMessage());
            }
        }

        // проверяем, найден ли класс
        if (objClass == null) {
            // если класс не найден, дальше делать нечего
            // показываем вместо формы сообщение от ошибке
            feedbackMessage = messages.get("crud-entity-class-not-found");
            return null;
        }

        // запоминаем класс
        entityClass = objClass;

        // ---------------- имя класса сущности - конец ------------------------

        // ---------------- объект сущности - начало ---------------------------
        IEntity objEntity = null;
        if (context.getCount() >= 2) {
            // имя класса есть, достаем из параметров объект
            objEntity = (IEntity) context.get(entityClass, 1);
        }

        // найден объект или нет ?
        if (objEntity != null) {
            // находим объект в базе данных
            entityObject = entityService.find(entityClass, objEntity.getId());
        } else {
            return null;
        }
        // ---------------- объект сущности - конец ----------------------------

        // проверяем право на запись объектов текущего класса
        SecurityUtils.getSubject().checkPermission(entityClass.getSimpleName() + ":write");

        return null;
    }

    public Object[] onPassivate() {
        Object[] context = new Object[2];
        context[0] = this.entityClass.getSimpleName();
        context[1] = this.entityObject;
        return context;
    }

    /**
     * Название страницы
     * @return 
     */
    public String getTitle() {
        return (entityClass != null) ? messages.get("entity." + entityClass.getSimpleName()) : "Unknown entity";
    }

    ///**
    // * Сохранение объекта
    // */
    //public Object onSuccessFromEditForm() {
    //    feedbackMessage = null;
    //    try {
    //        // изменяем объект в базе данных
    //        SecurityUtils.getSubject().checkPermission(entityClass.getSimpleName() + ":update:" + entityObject.getId());
    //        entityService.merge(entityObject);
    //        feedbackMessage = messages.get("crud-changes-saved-successfully");
    //    } catch (RuntimeException ex) {
    //        LOG.error(ex.getClass().getName() + " " + ex.getMessage());
    //        feedbackMessage = messages.get("message.update-error");
    //    }
    //    return null;
    //}
    
    
    public Object onDelete() {
        feedbackMessage = null;
        try {
            //object = entityService.find(objectClass, id);
            //entityObject = entityService.find(entityClass, objEntity.getId());
            // entityObject=entityService.find(entityClass, id);
            entityService.remove(entityObject);
            feedbackMessage = messages.get("crud-entity-deleted-successfully");
            entityObject=null;
        } catch (RuntimeException ex) {
            LOG.error(ex.getClass().getName() + " " + ex.getMessage());
            feedbackMessage = messages.get("message.delete-error");
        }
        return null;
    }

    public boolean getIsEntityClassFound() {
        return entityClass != null;
    }

    public boolean getIsEntityObjectFound() {
        return entityObject != null;
    }

    public void setEntityClass(Class<? extends IEntity> _entityClass) {
        this.entityClass = _entityClass;
    }

    public void setEntityObject(IEntity _entityObject) {
        this.entityObject = _entityObject;
    }

    public IEntity getEntityObject() {
        return this.entityObject;
    }
}
