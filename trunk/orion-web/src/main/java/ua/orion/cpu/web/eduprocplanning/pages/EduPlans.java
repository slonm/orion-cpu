package ua.orion.cpu.web.eduprocplanning.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.web.services.TapestryDataSource;

/**
 *
 * @author sl
 */
@Import(library = "classpath:ua/orion/web/WindowUtils.js",
stylesheet = "classpath:ua/orion/web/css/tapestry-crud.css")
@SuppressWarnings("unused")
public class EduPlans {
    //---Components and component's resources---

    @Component
    private Zone listZone;
    @Inject
    private Block editBlock;
    @Inject
    private Block deleteBlock;
    @Inject
    @Property(write = false)
    private Messages messages;
    //---Services---
    @Inject
    private Logger LOG;
    @Inject
    private EntityService entityService;
    @Inject
    @Property(write = false)
    private TapestryDataSource dataSource;
    //---Locals---
    private Class<EduPlan> objectClass = EduPlan.class;
    @Persist
    @Property
    private EduPlan object;
    @Persist
    @Property(write = false)
    private String mode;
    @Property(write = false)
    private String error;
    private static final String EDIT = "edit";
    private static final String ADD = "add";
    private static final String DEL = "del";
    @Environmental
    private JavaScriptSupport javascriptSupport;
    /*
     * Свойство, отвечающее за отображение всплывающих подсказок
     */
    @Inject
    @Symbol(OrionCPUSymbols.SHOW_HINTS)
    @Property
    private String showHints;

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public Object onActivate(EventContext context) {
        SecurityUtils.getSubject().checkPermission("EduPlan:read");
        return null;
    }

    public String getId() {
        return entityService.getPrimaryKey(object).toString();
    }

    public GridDataSource getObjects() {
        //Инициализация иконок при изменениях в получении данных
        javascriptSupport.addInitializerCall(InitializationPriority.valueOf("NORMAL"), "initializeIcons", "");
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

    public Object onEdit(EduPlan object) {
        SecurityUtils.getSubject().checkPermission("EduPlan:update:" + object.getId());
        mode = EDIT;
        this.object = object;
        error = null;
        return editBlock;
    }

    public Object onAdd() {
        SecurityUtils.getSubject().checkPermission("EduPlan:insert");
        mode = ADD;
        object = entityService.newInstance(objectClass);
        error = null;
        return editBlock;
    }

    public Object onTryDelete(EduPlan object) {
        SecurityUtils.getSubject().checkPermission("EduPlan:delete:" + object.getId());
        mode = DEL;
        this.object = object;
        error = null;
        return deleteBlock;
    }

    public Object onDelete(EduPlan object) {
        SecurityUtils.getSubject().checkPermission("EduPlan:delete:" + object.getId());
        error = null;
        try {
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
