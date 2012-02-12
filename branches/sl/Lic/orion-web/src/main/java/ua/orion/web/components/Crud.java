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
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import ua.orion.core.services.EntityService;
import ua.orion.web.CurrentBeanContext;
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
    private Block addBlock;
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
    @Persist
    private Object object;
    @Persist
    @Property(write = false)
    private String popupWindowId;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    @Inject
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
        popupWindowId = javascriptSupport.allocateClientId("popupWindow");
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
        //Это хак.
        //Обновление grid. Нужно для применения классов CSS, некоторых функций JS.
        //TODO Продумать как привязаться к обновлению внутренней зоны Grid
        javascriptSupport.addInitializerCall("updateGrid", "");
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
        return closeWindowAndGetListZone();
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
        return closeWindowAndGetListZone();
    }

    public Object onEdit(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":update:" + id);
        object = es.find(objectClass, id);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.edit")));
            }
        });
        return editBlock;
    }

    public Object onAdd() {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":insert");
        object = es.newInstance(objectClass);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.add")));
            }
        });
        return addBlock;
    }

    public Object onView(Integer id) {
        object = es.find(objectClass, id);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.view")));
            }
        });
        return viewBlock;
    }

    public Object onTryDelete(Integer id) {
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":delete:" + id);
        object = es.find(objectClass, id);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.del"),
                        //TODO Размер окна должен вычислятся автоматически
                        "width", "235"));
            }
        });
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
        return closeWindowAndGetListZone();
    }

    private Object closeWindowAndGetListZone() {
        //Close window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("closeCkWindow",
                        new JSONObject("window", popupWindowId));
            }
        });
        return listZone.getBody();
    }
}
