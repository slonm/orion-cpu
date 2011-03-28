package orion.cpu.views.tapestry.pages.crud;

import java.beans.IntrospectionException;
import java.util.List;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.impl.GridFieldCalculable;
import orion.tapestry.grid.lib.hibernate.GridModelHibernateBean;
import orion.tapestry.grid.lib.model.GridModelInterface;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.services.GridTypeMap;

/**
 * Список записей для изменения/удаления/добавления
 * + метод для удаления записей
 * @author dobro
 */
@SuppressWarnings("unused")
@IncludeStylesheet({"window/css/default.css", "window/css/spread.css"})
@IncludeJavaScriptLibrary({"window/js/window.js", "window/js/window_ext.js", "window/js/debug.js", "CrudList.js"})
public class CrudList {

    /**
     * Конфигурация компоненты Grid
     */
    @Inject
    private GridTypeMap typeMap;
    /**
     * Сообщения интерфейса
     */
    @Inject
    private Messages messages;
    /**
     * Соединение с БД
     */
    @Inject
    private HibernateSessionManager sm;
    /**
     * Logger class
     */
    private static final Logger LOG = LoggerFactory.getLogger(CrudList.class);
    /**
     * Entity class used by grid model
     */
    private Class entityClass;
    private ClassMetadata entityClassMetadata;
    private String entityClassIdentifierName;
    /**
     * Временная переменная для цикла по колонкам
     * Цикл объявлен в шаблоне
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridFieldAbstract currentField;
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridRow currentRow;

    /**
     * page title
     */
    @Property
    private String title;
    //    /**
//     * Configuration parameter.
//     * Root project package.
//     */
//    @Inject
//    @Symbol("orion.root-package")
//    private String rootPackage;
//    /**
//     * Configuration parameter.
//     * Package where entities classes are located.
//     */
//    @Inject
//    @Symbol("orion.entities-package")
//    private String entitiesPackage;
//    /**
//     * Main navigation menu path
//     */
//    @Property
//    private Object menuPath;
//    /**
//     * HTTP Request
//     */
//    @Inject
//    private Request request;
//    /**
//     * Type of the entity primary key
//     */
//    private Class entityPrimaryKeyClass;
    /**
     * При открытии страницы извлекаем из параметров класс сущности
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {
        try {
            // at east one parameter is required
            assert context.getCount() > 0;
            String entityClassName = context.get(String.class, 0);

            // get class by name
            this.entityClass = Class.forName(entityClassName);
            //
        } catch (Exception ex) {
            LOG.debug("Invalid activation context. Redirect to root page");
            return "";
        }

        // get hibernate metadata for a given entity
        this.entityClassMetadata = sm.getSession().getSessionFactory().getClassMetadata(this.entityClass);

        // get identifier type name
        this.entityClassIdentifierName = this.entityClassMetadata.getIdentifierPropertyName();

        // todo добавить проверку прав доступа к классу

        // check permission
        //getAuthorizer().checkSearch(getEntityClass());

        // get page title
        title = messages.get(this.entityClass.getName());// "reflect." +

        // get menu path
        // menuPath = request.getParameter("menupath");
        return null;
    }

    public Object onPassivate() {
        return this.entityClass.getName();
    }

    /**
     * Get identifier of the entity object
     */
    public Object getId(GridRow row) {
        return row.getValue(this.entityClassIdentifierName);
    }

    /**
     * Get identifier of the entity object
     */
    public String getEntityClassName() {
        return this.entityClass.getName();
    }

    public Object[] getCurrentRowContext() {
        Object[] tor = {this.entityClass.getName(), this.currentRow.getValue(this.entityClassIdentifierName)};
        return tor;
    }

    public Object getCurrentRowId() {
        return this.currentRow.getValue(this.entityClassIdentifierName);
    }


    /**
     * @return Модель данных для grid
     */
    public GridModelInterface getGridModel() {
        GridModelInterface gridmodel = null;
        try {
            Session session = sm.getSession();
            gridmodel = new GridModelHibernateBean(this.entityClass, typeMap.getConfiguration(), messages, session);
            // add empty column
            gridmodel.addField(new GridFieldCalculable("rowActions"));
            // todo добавить дополнительные условия выборки, в зависимости от прав пользователя
        } catch (HibernateException ex) {
            LOG.debug(this.getClass().getName() + ":HibernateException:" + ex.getMessage());
        } catch (IntrospectionException ex) {
            LOG.debug(this.getClass().getName() + ":IntrospectionException:" + ex.getMessage());
        }
        return gridmodel;
    }

    Object onActionFromCRUDDelete(Long rowId) {
        try {
            Session session=sm.getSession();
            Criteria criteria = session.createCriteria(this.entityClass).add(Restrictions.eq(this.entityClassIdentifierName, rowId));
                List result = criteria.list();
                if (!result.isEmpty()) {
                    Object entity = result.get(0);
                    session.delete(entity);
                    sm.commit();
                }
        } catch (IllegalArgumentException ex) {
            LOG.debug(this.getClass().getName() + ":IllegalArgumentException:" + ex.getMessage());
        } catch (HibernateException ex) {
            LOG.debug(this.getClass().getName() + ":HibernateException:" + ex.getMessage());
        }
        return null;
    }
    //CRUDEdit
}
