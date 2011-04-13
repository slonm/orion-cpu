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
import org.hibernate.Query;
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
    protected static final Logger LOG = LoggerFactory.getLogger(CrudList.class);
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
    private String title;

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
            this.entityClass = this.entityClassForName(entityClassName);

        } catch (Exception ex) {
            LOG.error("Invalid activation context. Redirect to root page. Error message is:"+ex.getMessage());
            return "";
        }

        // get hibernate metadata for a given entity
        this.entityClassMetadata = sm.getSession().getSessionFactory().getClassMetadata(this.entityClass);

        // get identifier type name
        this.entityClassIdentifierName = this.entityClassMetadata.getIdentifierPropertyName();

        // load page title
        title = messages.get(this.entityClass.getName());// "reflect." +


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
        // create grid model

        /**
         * Grid model
         */
        GridModelInterface gridmodel=null;
        try {
            Session session = sm.getSession();
            gridmodel = new GridModelHibernateBean(this.entityClass, typeMap.getConfiguration(), session) {

                @Override
                public String customFilterJSON() {
                    return setCustomFilterJSON();
                }
            };
            // add empty column
            gridmodel.addField(new GridFieldCalculable("rowActions"));

        } catch (HibernateException ex) {
            LOG.debug(this.getClass().getName() + ":HibernateException:" + ex.getMessage());
        } catch (IntrospectionException ex) {
            LOG.debug(this.getClass().getName() + ":IntrospectionException:" + ex.getMessage());
        }
        return gridmodel;
    }

    Object onActionFromCRUDDelete(Long rowId) {
        try {
            Session session = sm.getSession();
            Query query=session.createQuery("from "+this.entityClass.getSimpleName()+" where "+this.entityClassIdentifierName+"="+rowId);
            List result = query.list();
            //Criteria criteria = session.createCriteria(this.entityClass).add(Restrictions.eq(this.entityClassIdentifierName, rowId));
            //List result = criteria.list();


            if (!result.isEmpty()) {
                Object entity = result.get(0);
                session.delete(entity);
                sm.commit();
            }
        } catch (IllegalArgumentException ex) {
            LOG.debug(this.getClass().getName() + ":IllegalArgumentException:" + ex.getMessage());
        } catch (HibernateException ex) {
            LOG.debug(this.getClass().getName() + ":HibernateException:" + ex.getMessage());
        } catch(Exception ex){
            LOG.debug(this.getClass().getName() + ":Exception:" + ex.getMessage());
        }
        return null;


    }

    /**
     * Возвращает дополнительное условие фильтрации в формате JSON.
     * Структура условия описана в комментариях к методу customFilterJSON()
     * интерфейса {@link orion.tapestry.grid.lib.model.GridModelInterface}
     * Чтобы добавить в {@link orion.tapestry.grid.components.Grid}
     * дополнительные условия отбора строк, надо создать наследника
     * класса {@link orion.cpu.views.tapestry.pages.crud.CrudList}
     * перекрыть этот метод.
     * @return дополнительные условия фильтрации в формате JSON
     */
    public String setCustomFilterJSON() {
        return null;


    }

    /**
     * Создаёт класс по имени.
     * Наличие метода позволяет организовать создание класса по сокращённому имени.
     * @param entityClassName полное имя класса
     * @return класс сущности
     */
    public Class entityClassForName(String entityClassName) throws ClassNotFoundException {
        return Class.forName(entityClassName);


    }

    public Class getEntityClass() {
        return this.entityClass;


    }

    public String getEntityClassIdentifierName() {
        return this.entityClassIdentifierName;


    }

    public ClassMetadata getEntityClassMetadata() {
        return this.entityClassMetadata;


    }

    public void setTitle(String _title) {
        this.title = _title;


    }

    public String getTitle() {
        return this.title;

    }
}
