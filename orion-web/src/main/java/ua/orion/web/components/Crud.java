package ua.orion.web.components;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.web.services.TapestryDataSource;
import ua.orion.web.services.TipService;

/**
 * Компонент, который предоставляет CRUD для сущностей
 *
 * @author slobodyanuk
 */
@Import(stack = {"orion"})
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
     * строки появится кнопка перехода на эту страницу. Страница в контексте
     * должна принимать объект, с которым она будет работать
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
    private TipService tip;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    @Inject
    private JavaScriptSupport javascriptSupport;
    @Inject
    private Logger LOG;
    @Inject
    private PageRenderLinkSource pageRenderLinkSource;
    //---Locals---
    @Persist
    private Object object;
    @Persist
    private String listZoneId;
    @Persist
    @Property(write = false)
    private String popupZoneId;

    void setupRender() {
        SecurityUtils.getSubject().checkPermission(entityType + ":read");
        listZoneId = javascriptSupport.allocateClientId("listZone");
        popupZoneId = javascriptSupport.allocateClientId("popupZone");
        javascriptSupport.addInitializerCall("crudUIListeners", new JSONObject("popupZone", popupZoneId, "listZone", listZoneId));
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
        if (resources.isBound("source")) {
            return source;
        } else {
            return dataSource.getGridDataSource(getEntityClass());
        }
    }

    /**
     * Обработчик сохранения после редактирования. Может использоваться
     * переопределенной формой редактирования, если она редактировала свойство
     * Object компонента Crud.
     */
    public Object onSuccessFromEditForm() {
        resources.triggerEvent("beforeEdit", new Object[]{object}, null);
        tip.doWork(new Runnable() {

            @Override
            public void run() {
                es.merge(object);
                resources.triggerEvent("afterEdit", new Object[]{object}, null);
            }
        }, "update.entity", messages.get("entity." + entityType), es.getStringValue(object));
        return closeWindowAndGetListZone();
    }

    /**
     * Обработчик сохранения после создания нового объекта. Может использоваться
     * переопределенной формой редактирования, если она редактировала свойство
     * Object компонента Crud Если задан параметр detailPage, то метод
     * возвращает перенаправление на эту страницу с контекстом - только-что
     * сохраненным объектом.
     */
    public Object onSuccessFromAddForm() {
        resources.triggerEvent("beforeAdd", new Object[]{object}, null);
        tip.doWork(new Runnable() {

            @Override
            public void run() {
                es.persist(object);
                resources.triggerEvent("afterAdd", new Object[]{object}, null);
            }
        }, "insert.entity", messages.get("entity." + entityType), es.getStringValue(object));
        if (resources.isBound("detailPage")) {
            return pageRenderLinkSource.createPageRenderLinkWithContext(detailPage, object);
        } else {
            return closeWindowAndGetListZone();
        }
    }

    Object onActionFromEdit(Integer id) {
        SecurityUtils.getSubject().checkPermission(entityType + ":update:" + id);
        object = es.find(getEntityClass(), id);
        resources.triggerEvent("beforeEditPopup", new Object[]{object}, null);
        return editBlock;
    }

    Object onActionFromAdd() {
        SecurityUtils.getSubject().checkPermission(entityType + ":insert");
        object = es.newInstance(getEntityClass());
        resources.triggerEvent("beforeAddPopup", new Object[]{object}, null);
        return addBlock;
    }

    Object onActionFromView(Integer id) {
        object = es.find(getEntityClass(), id);
        resources.triggerEvent("beforeViewPopup", new Object[]{object}, null);
        return viewBlock;
    }

    Object onDelete(Integer id) {
        LOG.debug("Delete entity id={}", id.toString());
        object = es.find(getEntityClass(), id);
        resources.triggerEvent("beforeDelete", new Object[]{object}, null);
        tip.doWork(new Runnable() {

            @Override
            public void run() {
                es.remove(object);
            }
        }, "remove.entity", messages.get("entity." + entityType), es.getStringValue(object));
        return closeWindowAndGetListZone();
    }

    private Object closeWindowAndGetListZone() {
        //Close window
        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

            @Override
            public void run(JavaScriptSupport js) {
                js.addInitializerCall("oriDialog", new JSONObject("oriEvent", "close", "oriId", popupZoneId));
            }
        });
        return listZone.getBody();
    }
}
