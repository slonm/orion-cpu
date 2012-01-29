package ua.orion.web.components;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.alerts.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import ua.orion.core.services.EntityService;
import ua.orion.web.CurrentBeanContext;
import ua.orion.web.OrionWebSymbols;
import ua.orion.web.services.TapestryDataSource;

/**
 * Компонент, который предоставляет CRUD для сущностей
 * @author slobodyanuk
 */
@Import(library = "../WindowUtils.js", stylesheet = "../css/tapestry-crud.css")
@SuppressWarnings("unused")
public class Crud {
    //---Components and component's resources---

    @Component
    private Zone listZone;
    @Inject
    @Property(write = false)
    private Block buttons;
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
    private EntityService es;
    @Inject
    private ComponentResources resources;
    @Inject
    @Property(write = false)
    private TapestryDataSource dataSource;
    @Inject
    private Environment environment;
    @Inject
    private AlertManager alertManager;
    //---Locals---
    @Parameter(allowNull = false)
    private Class<?> objectClass;
    @Parameter(allowNull = false)
    private GridDataSource source;
    @Parameter(value = "true", autoconnect = true)
    @Property
    private boolean showEditButton;
    @Parameter(value = "true", autoconnect = true)
    @Property
    private boolean showViewButton;
    @Parameter(value = "true", autoconnect = true)
    @Property
    private boolean showDelButton;
    @Parameter(defaultPrefix = "literal")
    @Property
    private String detailPage;
    @Parameter(defaultPrefix = "literal")
    @Property
    private String listPage;
    /*
     * Свойство, отвечающее за отображение всплывающих подсказок
     */
    @Inject
    @Symbol(OrionWebSymbols.SHOW_HINTS)
    @Property
    private String showHints;
    @Persist
    private Object object;
    @Persist
    @Property(write = false)
    private String mode;
    private static final String EDIT = "edit";
    private static final String ADD = "add";
    private static final String VIEW = "view";
    private static final String DEL = "del";
    @Environmental
    private JavaScriptSupport javascriptSupport;
    private CurrentBeanContext currentBeanContext = new CurrentBeanContext() {

        @Override
        public Object getCurrentBean() {
            return object;
        }

        @Override
        public Class<?> getBeanType() {
            return objectClass;
        }

        @Override
        public String getCurrentBeanId() {
            return getId();
        }
    };

    void setupRender() {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":read");
        environment.push(CurrentBeanContext.class, currentBeanContext);
    }

    final void afterRender() {
        environment.pop(CurrentBeanContext.class);
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public Object getObject() {
        //это хак. 
        //у нас нет возможности удалить контекст после использования
        //его при обработке события компонента, поэтому не удаляем его вообще,
        //а просто проверяем тот ли он что и был. Такой подход приведет к ошибкам
        //при вложенных компонентах, устанавливающих CurrentBeanContext
        //TODO Продумать и переделать
        CurrentBeanContext cbc = environment.peek(CurrentBeanContext.class);
        if (cbc == null || cbc != currentBeanContext) {
            environment.push(CurrentBeanContext.class, currentBeanContext);
        }
        return object;
    }

    public String getId() {
        return es.getPrimaryKey(object).toString();
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public void setObject(Object object) {
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
    public void setObjectClass(Class<?> objectClass) {
        this.objectClass = objectClass;
    }

    public GridDataSource getSource() {
        //Обновление grid. Нужно для применения классов CSS, некоторых функций JS. 
        javascriptSupport.addInitializerCall(InitializationPriority.valueOf("NORMAL"), "updateGrid", "");
        if (resources.isBound("source")) {
            return source;
        } else {
            return dataSource.getGridDataSource(objectClass);
        }
    }

    public Object onSuccessFromEditForm() {
        try {
            es.merge(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success.update.entity",
                    messages.get("entity." + objectClass.getSimpleName()),
                    es.getStringValue(object)));
        } catch (RuntimeException ex) {
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error.update.entity",
                    messages.get("entity." + objectClass.getSimpleName()),
                    es.getStringValue(object)));
        }
        return listZone.getBody();
    }

    public Object onSuccessFromAddForm() {
        try {
            es.persist(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success.insert.entity",
                    messages.get("entity." + objectClass.getSimpleName()),
                    es.getStringValue(object)));
        } catch (RuntimeException ex) {
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error.insert.entity",
                    messages.get("entity." + objectClass.getSimpleName()),
                    es.getStringValue(object)));
        }
        return listZone.getBody();
    }

    public Object onEdit(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":update:" + id);
        mode = EDIT;
        object = es.find(objectClass, id);
        return editBlock;
    }

    public Object onAdd() {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":insert");
        mode = ADD;
        object = es.newInstance(objectClass);
        return editBlock;
    }

    public Object onView(Integer id) {
        mode = VIEW;
        object = es.find(objectClass, id);
        return viewBlock;
    }

    public Object onTryDelete(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":delete:" + id);
        mode = DEL;
        object = es.find(objectClass, id);
        return deleteBlock;
    }

    public Object onDelete(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":delete:" + id);
        try {
            object = es.find(objectClass, id);
            es.remove(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success.remove.entity",
                    messages.get("entity." + objectClass.getSimpleName()),
                    es.getStringValue(object)));
        } catch (RuntimeException ex) {
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error.remove.entity",
                    messages.get("entity." + objectClass.getSimpleName()),
                    es.getStringValue(object)));
        }
        return listZone.getBody();
    }

    public boolean getIsEdit() {
        return EDIT.equals(mode);
    }
}
