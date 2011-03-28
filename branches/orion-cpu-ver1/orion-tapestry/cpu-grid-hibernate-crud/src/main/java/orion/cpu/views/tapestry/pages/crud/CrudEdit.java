package orion.cpu.views.tapestry.pages.crud;

import java.util.List;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dobro
 */
public class CrudEdit {

    /**
     * Entity class used by grid model
     */
    @Persist
    private Class entityClass;
    /**
     * Entity class metadata
     */
    private ClassMetadata entityClassMetadata;
    /**
     * Entity identifier name
     */
    private String entityClassIdentifierName;
    /**
     * Entity identifier
     */
    @Persist
    private Object entityId;
    /**
     * Logger class
     */
    private static final Logger LOG = LoggerFactory.getLogger(CrudList.class);
    /**
     * Соединение с БД
     */
    @Inject
    private HibernateSessionManager sm;
    /**
     * Сущность, которую мы редактируем
     */
    @Persist
    @Property
    private Object entity;
    /**
     * Сервис, составляющий модель (метаданные)
     * необходимые для отображения и обработки формы
     */
    @Inject
    private BeanModelSource beanModelSource;
    /**
     * Сообщения интерфейса
     */
    @Inject
    private Messages messages;
    /**
     * Feedback message
     */
    @Persist
    private String feedbackMessage;

    /**
     * This page title
     */
    @Property
    private String title;

    /**
     * Creates a {@link BeanModel} and removes the primary key property from it.
     *
     * @return a {@link BeanModel}.
     */
    public BeanModel getBeanModel() {

        final BeanModel beanModel = beanModelSource.createEditModel(this.entityClass, messages);

        beanModel.exclude(this.entityClassIdentifierName);

        return beanModel;

    }

    /**
     * При открытии страницы извлекаем из параметров класс сущности
     * и его идентификатор
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {
        try {
            // at east one parameter is required
            assert context.getCount() >= 1;
            String entityClassName = context.get(String.class, 0);
            this.setEntityClass(entityClassName);
        } catch (Exception ex) {
            LOG.debug("Invalid activation context. Redirect to root page");
            return null;
        }

        if (context.getCount() > 1) {
            this.entityId = context.get(this.entityClassMetadata.getIdentifierType().getReturnedClass(), 1);
        } else {
            this.entityId = null;
        }
        // load or create entity
        if (this.entity == null) {
            // if entity does not exist load or create it
            this.entity = this.getEntity(this.entityId);
        }else{
            if(!this.entityClassMetadata.getIdentifier(entity, (SessionImplementor) sm.getSession()).equals(this.entityId) ){
                // reload if entityId is changed
                this.entity = this.getEntity(this.entityId);
            }
        }
        //else{System.out.println("Entity exists");}
        // todo добавить проверку прав доступа к классу

        // check permission
        //getAuthorizer().checkSearch(getEntityClass());

        // get page title
        title = messages.get("reflect." + this.entityClass.getName());
        // get menu path
        // menuPath = request.getParameter("menupath");

        //System.out.println("OnActivate - ok");
        return null;
    }

    /**
     * Сохраняем запись в базу данных
     */
    Object onSuccess() {
        Session session = sm.getSession();
        session.beginTransaction();
        session.saveOrUpdate(entity);
        this.entityId = this.entityClassMetadata.getIdentifier(entity, (SessionImplementor) session);
        sm.commit();
        this.setFeedbackMessage(messages.get("crud-data-saved-successfully"));
        return null;
    }

    Object[] onPassivate() {
        Object[] context;
        if (this.entityId == null) {
            context = new Object[1];
            context[0] = this.entityClass.getName();
        } else {
            context = new Object[2];
            context[0] = this.entityClass.getName();
            context[1] = this.entityId;
        }
        return context;
    }

    public String getFeedbackMessage() {
        String msg=(this.feedbackMessage == null) ? "" : this.feedbackMessage;
        this.feedbackMessage="";
        return msg;
    }

    public void setFeedbackMessage(String _feedbackMessage) {
        this.feedbackMessage = _feedbackMessage;
    }

    /**
     * Set entity class by name
     * and related metadata
     * @param entityClassName class name
     * @throws ClassNotFoundException occurs if entity class not found
     *
     */
    public void setEntityClass(String entityClassName) throws ClassNotFoundException {
        // get class by name
        this.entityClass = Class.forName(entityClassName);

        // get hibernate metadata for a given entity
        this.entityClassMetadata = sm.getSession().getSessionFactory().getClassMetadata(this.entityClass);

        // get entity identifier name
        this.entityClassIdentifierName = this.entityClassMetadata.getIdentifierPropertyName();
    }


    /**
     * Load or create entity
     * Entity is loaded from database if identifier is set
     * @param _entityId entity identifier
     * @return entity instance
     */
    public Object getEntity(Object _entityId) {
        try {
            if (entityId == null) {
                // create empty entity
                return this.entityClass.newInstance();
            } else {
                // load entity from database  if entity identifier is posted
                Session session = sm.getSession();



                Criteria criteria = session.createCriteria(entityClass).add(Restrictions.eq(this.entityClassIdentifierName, this.entityId));
                List result = criteria.list();
                if (result.isEmpty()) {
                    return this.entityClass.newInstance();
                } else {
                    return result.get(0);
                }
            }
        } catch (InstantiationException ex) {
            LOG.debug(CrudEdit.class.getName() + " instantiation error while creating " + this.entityClass.getName() + " : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            LOG.debug(CrudEdit.class.getName() + " access error while creating " + this.entityClass.getName() + " : " + ex.getMessage());
        }
        return null;
    }
}
