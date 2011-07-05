package ua.orion.web.components;

import java.beans.IntrospectionException;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.impl.GridFieldCalculable;
import orion.tapestry.grid.lib.model.GridModelInterface;
import orion.tapestry.grid.lib.rows.GridRow;
import orion.tapestry.grid.services.GridFieldFactory;
import orion.tapestry.grid.services.GridTypeMap;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.services.EntityService;
import ua.orion.web.GridModelJPABean;
import ua.orion.web.services.TapestryDataSource;

/**
 * Компонент, который предоставляет CRUD для сущностей
 * @author slobodyanuk
 */
@Import(library = "../WindowUtils.js",
stylesheet = "../css/tapestry-crud.css")
@SuppressWarnings("unused")
public class CrudEx {

    /**
     * Конфигурация компоненты Grid
     */
    @Inject
    private GridTypeMap typeMap;
    /**
     * Временная переменная для цикла по колонкам
     * Цикл объявлен в шаблоне
     */
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridFieldAbstract currentField;
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне
     */
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridRow currentRow;
    
    @Inject
    private PropertyAccess pa;
    @Inject
    private GridFieldFactory gridFieldFactory;
    @Inject
    private Logger LOG;
    /**
     * @return Модель данных для grid
     */
    public GridModelInterface getGridModel() {
        // create grid model

        /**
         * Grid model
         */
        GridModelInterface gridmodel = null;
        try {
            gridmodel = new GridModelJPABean(objectClass, typeMap.getConfiguration(),
                    gridFieldFactory, pa, entityService.getEntityManager());
            // add empty column
            gridmodel.addField(new GridFieldCalculable("rowActions"));

            // localize
            //gridmodel=( new GridModelLocalizator() ).localize(gridmodel);

        } catch (IntrospectionException ex) {
            LOG.error(this.getClass().getName() + ":IntrospectionException:" + ex.getMessage());
        }
        return gridmodel;
    }
    
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
    //---Services---
    @Inject
    private Request request;
    @Inject
    private EntityService entityService;
    @Inject
    @Property(write = false)
    private TapestryDataSource dataSource;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    //---Locals---
    @Parameter(allowNull = false)
    private Class<? extends IEntity> objectClass;
    @Persist
    private IEntity object;
    @Persist
    @Property(write = false)
    private String mode;
    @Property(write = false)
    private String error;
    private static final String EDIT = "edit";
    private static final String ADD = "add";
    private static final String VIEW = "view";
    private static final String DEL = "del";

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    void setupRender() {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":read");
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
}
