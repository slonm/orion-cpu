package ua.orion.web.components;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import ua.orion.core.services.EntityService;
import ua.orion.web.services.TapestryDataSource;

/**
 * Компонент, который предоставляет CRUD для сущностей
 *
 * @author slobodyanuk
 */
@Import(library = "../WindowUtils.js", stylesheet = "../css/tapestry-crud.css")
@SuppressWarnings("unused")
public class Crud {

    //---Parameters---
    /**
     * Имя типа сущности
     */
    @Parameter(allowNull = false, defaultPrefix = "literal")
    @Property
    private String entityType;
    /**
     * Источник данных для Grid
     */
    @Parameter(allowNull = false)
    private GridDataSource source;
    /**
     * Показывать ли кнопку редактирования
     */
    @Parameter(value = "true", autoconnect = true)
    @Property
    private boolean showEditButton;
    /**
     * Показывать ли кнопку просмотра
     */
    @Parameter(value = "true", autoconnect = true)
    @Property
    private boolean showViewButton;
    /**
     * Показывать ли кнопку удаления
     */
    @Parameter(value = "true", autoconnect = true)
    @Property
    private boolean showDelButton;
    /**
     * Адрес detail страницы. Если страница задана, то в наборе кнопок CRUD
     * строки появится кнопка перехода на эту страницу
     */
    @Parameter(defaultPrefix = "literal")
    @Property
    private String detailPage;
    /**
     * master сущность. Если задана, то появится кнопка перехода на CRUD
     * страницу этой сущности
     */
    @Parameter
    @Property
    private Object master;
    /**
     * Блок переопределяющий форму редактирования сущности, предоставляемый по
     * умолчанию
     */
    @Parameter(value = "block:defaultEditBlock")
    private Block editBlock;
    /**
     * Блок переопределяющий форму добавления сущности, предоставляемый по
     * умолчанию
     */
    @Parameter(value = "block:defaultAddBlock")
    private Block addBlock;
    /**
     * Блок переопределяющий разметку представления сущности, предоставляемый по
     * умолчанию
     */
    @Parameter(value = "block:defaultViewBlock")
    private Block viewBlock;
    /**
     * Блок дополнительных кнопок панели инструментов
     */
    @Parameter
    @Property(write = false)
    private Block toolButtons;
    //---Components and component's resources---
    @Component
    private Zone listZone;
    @Inject
    private Block hideWindowButtonBlock;
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
    private AlertManager alertManager;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    @Inject
    private JavaScriptSupport javascriptSupport;
    //---Locals---
    @Persist
    private Object object;
    @Persist
    @Property(write = false)
    private String popupWindowId;
    @Persist
    private String listZoneId;
    @Persist
    @Property(write = false)
    private String popupZoneId;

    void setupRender() {
        SecurityUtils.getSubject().checkPermission(entityType + ":read");
        popupWindowId = javascriptSupport.allocateClientId("popupWindow");
        listZoneId = javascriptSupport.allocateClientId("listZone");
        popupZoneId = javascriptSupport.allocateClientId("popupZone");
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public Object getObject() {
        return object;
    }

    /**
     * Идентификатор текущего объекта
     */
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
    public Class<?> getEntityClass() {
        return es.getMetaEntity(entityType).getEntityClass();
    }

    /**
     * Используется для обновления зоны списка из переопределенных редакторов
     * editForm и addForm
     */
    public String getListZoneId() {
        return listZoneId;
    }

    /**
     * Используется для добавления кнопки закрытия окна из переопределенных
     * блоков, располагающихся во всплывающем окне
     */
    public Block getHideWindowButtonBlock() {
        return hideWindowButtonBlock;
    }

    /**
     * Адрес master страницы.
     */
    public String getMasterCrudPage() {
        return dataSource.getCrudPage(master.getClass());
    }

    public boolean getCanAdd() {
        if (resources.isBound("master")
                && !SecurityUtils.getSubject().isPermitted(master.getClass().getSimpleName() + ":update:" + es.getPrimaryKey(master))) {
            return false;
        }
        return SecurityUtils.getSubject().isPermitted(entityType + ":insert");
    }

    public GridDataSource getSource() {
        //Это хак.
        //Обновление grid. Нужно для применения классов CSS, некоторых функций JS.
        //TODO Продумать как привязаться к обновлению внутренней зоны Grid
        javascriptSupport.addInitializerCall("updateGrid", "");
        if (resources.isBound("source")) {
            return source;
        } else {
            return dataSource.getGridDataSource(getEntityClass());
        }
    }

    /**
     * Обработчик сохранения после редактирования. Может использоваться
     * переопределенной формой редактирования, если она редактировала свойство
     * Object компонента Crud
     */
    public Object onSuccessFromEditForm() {
        SecurityUtils.getSubject().checkPermission(entityType + ":update:" + getId());
        try {
            es.merge(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success.update.entity",
                    messages.get("entity." + entityType),
                    es.getStringValue(object)));
        } catch (RuntimeException ex) {
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error.update.entity",
                    messages.get("entity." + entityType),
                    es.getStringValue(object)));
        }
        return closeWindowAndGetListZone();
    }

    /**
     * Обработчик сохранения после создания нового объекта. Может использоваться
     * переопределенной формой редактирования, если она редактировала свойство
     * Object компонента Crud
     */
    public Object onSuccessFromAddForm() {
        SecurityUtils.getSubject().checkPermission(entityType + ":insert");
        try {
            es.persist(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success.insert.entity",
                    messages.get("entity." + entityType),
                    es.getStringValue(object)));
        } catch (RuntimeException ex) {
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error.insert.entity",
                    messages.get("entity." + entityType),
                    es.getStringValue(object)));
        }
        return closeWindowAndGetListZone();
    }

    Object onEdit(Integer id) {
        SecurityUtils.getSubject().checkPermission(entityType + ":update:" + id);
        object = es.find(getEntityClass(), id);
        resources.triggerEvent("beforeEditPopup", new Object[]{object}, null);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.edit")));
            }
        });
        return editBlock;
    }

    Object onAdd() {
        SecurityUtils.getSubject().checkPermission(entityType + ":insert");
        object = null;
        resources.triggerEvent("newInstance", new Object[]{}, new ComponentEventCallback() {

            @Override
            public boolean handleResult(Object result) {
                object = result;
                return true;
            }
        });
        if (object == null) {
            object = es.newInstance(getEntityClass());
        }
        resources.triggerEvent("beforeAddPopup", new Object[]{object}, null);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.add")));
            }
        });
        return addBlock;
    }

    Object onView(Integer id) {
        object = es.find(getEntityClass(), id);
        resources.triggerEvent("beforeViewPopup", new Object[]{object}, null);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("showCkWindow",
                        new JSONObject("window", popupWindowId,
                        "title", messages.get("label.mode.view")));
            }
        });
        return viewBlock;
    }

    Object onTryDelete(Integer id) {
        SecurityUtils.getSubject().checkPermission(entityType + ":delete:" + id);
        object = es.find(getEntityClass(), id);
        resources.triggerEvent("beforeTryDeletePopup", new Object[]{object}, null);
        //Show window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
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

    Object onDelete(Integer id) {
        SecurityUtils.getSubject().checkPermission(entityType + ":delete:" + id);
        try {
            object = es.find(getEntityClass(), id);
            es.remove(object);
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success.remove.entity",
                    messages.get("entity." + entityType),
                    es.getStringValue(object)));
        } catch (RuntimeException ex) {
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error.remove.entity",
                    messages.get("entity." + entityType),
                    es.getStringValue(object)));
        }
        return closeWindowAndGetListZone();
    }

    private Object closeWindowAndGetListZone() {
        //Close window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
            public void run(JavaScriptSupport javascriptSupport) {

                javascriptSupport.addInitializerCall("closeCkWindow",
                        new JSONObject("window", popupWindowId));
            }
        });
        return listZone.getBody();
    }
//    void onInplaceUpdateFromGrid(String zone) {
//        javascriptSupport.addScript("Tapestry.Initializer.updateGrid()");
//        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {
//
//            public void run(JavaScriptSupport javascriptSupport) {
//
//                javascriptSupport.addInitializerCall("updateGrid", "");
//            }
//        });
//    }
}
