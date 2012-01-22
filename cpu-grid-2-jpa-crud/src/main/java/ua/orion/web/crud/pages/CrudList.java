package ua.orion.web.crud.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import orion.tapestry.grid.lib.datasource.DataSource;
import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;
import orion.tapestry.grid.services.CpuGridDataSourceFactory;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.persistence.MetaEntity;
import java.util.List;
import java.util.PriorityQueue;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.lib.MenuItem;
import ua.orion.tapestry.menu.lib.PageMenuLink;
import ua.orion.tapestry.menu.services.OrionMenuService;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * и использует cpu-grid
 * @author dobro
 */
@SuppressWarnings("unused")
@Import(stylesheet = {"window/css/default.css", "window/css/spread.css", "crud.css"},
library = {"window/js/window.js", "window/js/window_ext.js", "window/js/debug.js", "CrudList.js"})
public class CrudList {

    /**
     * Generic version of {@link javax.servlet.http.HttpServletRequest}, 
     * used to encapsulate the Servlet API version, and to
     * help bridge the differences between Servlet API and Porlet API.
     * <p/>
     * <p/>
     * The Request service is a 
     * {@linkplain org.apache.tapestry5.ioc.services.PropertyShadowBuilder shadow} 
     * of the current thread's request.
     */
    @Inject
    private Request request;
    /**
     * Логгер
     */
    @Inject
    private Logger LOG;
    /**
     * Provides a component instance with the resources provided by the framework. 
     * In many circumstances, the resources object can be considered the component 
     * itself; in others, it is the {@linkplain #getComponent() component property},
     * an instance of a class provided by the application developer (though 
     * transformed in many ways while being loaded) that is the true component. 
     * In reality, it is the combination of the resources object with the user 
     * class instance that forms the components; neither is useful without the other.
     */
    @Inject
    private ComponentResources resources;
    /**
     * ??????
     */
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    /**
     * Сообщения интерфейса
     */
    @Inject
    @Property(write = false)
    private Messages messages;
    /**
     * Константа - имя стартовой страницы
     */
    @Inject
    @Symbol(SymbolConstants.START_PAGE_NAME)
    private String startPageName;
    /**
     * Сервис-создатель источников данных
     */
    @Inject
    private CpuGridDataSourceFactory cpuGridDataSourceFactory;
    /**
     * Класс сущности, с которой будем работать
     */
    private Class<? extends IEntity> objectClass;
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне компоненты Grid
     */
    @SuppressWarnings("unused")
    @Property
    private IEntity currentRow;
    ///**
    // * Сохранённые настройки таблицы
    // */
    //@Persist
    //@Property
    //private IGridSettingStore settingStore;
    /**
     * Константа - навигационное меню
     */
    @Persist
    @Property
    private List<MenuData> menuBar;
    /**
     * Севис - построитель меню
     */
    @Inject
    private OrionMenuService cpuMenu;

    /**
     * Источник данных таблицы
     */
    public DataSource getDataSource() {
        return cpuGridDataSourceFactory.createDataSource(objectClass);
    }

    /**
     * Переопределение этого метода в наследниках позволит кастомизировать титул страниц
     * @return 
     */
    public String getTitle() {
        return messages.get("entity." + objectClass.getSimpleName());
    }

    /**
     * Возвращает класс сущности, с которой будем работать
     */
    public Class<?> getObjectClass() {
        return objectClass;
    }

    /**
     * Устанавливает класс сущности, с которой будем работать
     */
    public void setObjectClass(Class<? extends IEntity> objectClass) {
        this.objectClass = objectClass;
    }

    public Object onActivate(EventContext context) {
        // тип сущности
        Class<? extends IEntity> objClass = null;

        // надо проверить, было ли отправлено новое имя класса
        if (context.getCount() >= 1) {
            // имя класса есть, достаем класс
            objClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();

        }

        //if (objClass != null && !objClass.equals(objectClass)) {
        // извлекаем из контекста активации класс сущности
        if (objClass != null) {
            objectClass = objClass;
        } else {
            return this.startPageName;
        }

        // Читаем описание меню
        String postedMenuPath = request.getParameter("menupath");

        // если было отправлено новое описание меню
        // значит старое забываем, а новое запоминаем
        if (postedMenuPath != null) {
            menuBar = cpuMenu.getMenu(postedMenuPath, null, null, null);

            // добавляем кнопку "Создать"
            //if(SecurityUtils.getSubject().isPermitted(objectClass.getSimpleName() + ":insert")){
            //    MenuItem mi=new MenuItem(postedMenuPath+">action.add", new PageMenuLink("crud/"+CrudCreate.class.getSimpleName(), this.getCrudRowContext()));
            //    MenuData addItemMenu=new MenuData(mi, new PriorityQueue<MenuItem>());
            //    menuBar.add(addItemMenu);
            //}
            //LOG.info("save menu path "+postedMenuPath);
        }

        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":read");
        return null;
    }

    public Object onPassivate() {
        return this.objectClass.getSimpleName();
    }

    public Object[] getCrudRowContext() {
        Object[] context;
        if (this.currentRow == null) {
            context = new Object[1];
            context[0] = this.objectClass.getSimpleName();
        } else {
            context = new Object[2];
            context[0] = this.objectClass.getSimpleName();
            context[1] = this.currentRow;
            //context[2] = this.menupath;
        }
        return context;
    }
}
