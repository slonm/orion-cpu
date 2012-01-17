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

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * и использует cpu-grid
 * @author dobro
 */
@SuppressWarnings("unused")
@Import(stylesheet = {"window/css/default.css", "window/css/spread.css","crud.css"},
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
    ///**
    // * Хранит последнюю запрошенную страницу
    // */
    //@Inject
    //private LastPageHolder lastPageHolder;
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
    @Persist
    private Class<? extends IEntity> objectClass;
    ///**
    // * Сохранённые настройки таблицы
    // */
    //@Persist
    //@Property
    //private IGridSettingStore settingStore;
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне компоненты Grid
     */
    @SuppressWarnings("unused")
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private IEntity currentRow;
    /**
     * Константа - путь в дереве меню
     */
    @Persist
    @Property
    private String menupath;

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
        Class<? extends IEntity> objClass = null;
        // надо проверить, было ли отправлено новое имя класса
        if (context.getCount() >= 1) {
            // имя класса есть, достаем класс
            objClass = (Class<? extends IEntity>) context.get(MetaEntity.class, 0).getEntityClass();

        }
        // сравниваем с тем, что было раньше
        // если новое имя отличается от старого, то запомнить 
        // новые значения
        if (objClass != null && !objClass.equals(objectClass)) {
            // если новый класс отличается от старого, запоминаем новый класс
            objectClass = objClass;
        }

        // Читаем описание меню
        String postedMenuPath = request.getParameter("menupath");

        // если было отправлено новое описание меню
        // и это новое описание не совпадает со старым
        // значит старое забываем, а новое запоминаем
        if (postedMenuPath != null && !postedMenuPath.equals(menupath)) {
            menupath = postedMenuPath;
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
