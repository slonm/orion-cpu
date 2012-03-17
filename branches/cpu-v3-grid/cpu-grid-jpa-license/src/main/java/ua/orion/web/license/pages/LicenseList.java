package ua.orion.web.license.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import orion.tapestry.grid.lib.datasource.DataSource;
//import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;
import orion.tapestry.grid.services.CpuGridDataSourceFactory;
import ua.orion.core.persistence.IEntity;
import ua.orion.core.persistence.MetaEntity;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import orion.tapestry.grid.lib.datasource.impl.JPADataSource;
import orion.tapestry.grid.lib.model.bean.GridBeanModel;
import orion.tapestry.grid.lib.restrictioneditor.RestrictionEditorJPACriteria;
import orion.tapestry.grid.services.GridBeanModelSource;
import ua.orion.core.services.ModelLabelSource;
import ua.orion.cpu.core.licensing.entities.License;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.services.OrionMenuService;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * и использует cpu-grid
 * @author dobro
 */
@SuppressWarnings("unused")
@Import(stylesheet = {"window/css/default.css", "window/css/spread.css", "LicenseList.css"},
library = {"window/js/window.js", "window/js/window_ext.js", "window/js/debug.js", "LicenseList.js"})
public class LicenseList {

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
    //    /**
    //     * ??????
    //     */
    //    @Inject
    //    private ComponentEventLinkEncoder componentEventLinkEncoder;
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
     * Сервис для создания модели, описывающей таблицу
     */
    @Inject
    private GridBeanModelSource gridBeanModelSource;
    /**
     * JPA EntityManager - подключение к базе данных
     */
    @Inject
    private EntityManager entityManager;
    /**
     * Сервис-источник подписей,
     * который выполняет поиск в нескольких местах.
     */
    @Inject
    private ModelLabelSource modelLabels;
    //    /**
    //     * Компонента, которая отображает список
    //     */
    //    @Component(id="cpuGrid")
    //    private Grid grid;

    /**
     * Источник данных таблицы
     */
    public DataSource getDataSource() {
        GridBeanModel gridBeanModel = getModel();
        RestrictionEditorJPACriteria editor = new RestrictionEditorJPACriteria(objectClass, entityManager);
        JPADataSource dataSource = new JPADataSource(gridBeanModel, entityManager, editor) {

            /**
             * Применяет дополнительные условия выборки строк
             * этот метод следует перекрывать
             * @param criteria
             */
            @Override
            protected void applyAdditionalConstraints(CriteriaQuery criteriaQuery) {
                //                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                //                Predicate restriction=criteriaQuery.getRestriction();
                //                if(restriction==null){
                //                    //Predicate predicate = criteriaBuilder.ge(LOG, LOG)//("pint", 10);
                //                }else{
                //                }
            }
        };
        return dataSource;
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

    /**
     * Выполняется перед каждой загрузкой страницы
     */
    //public Object onActivate(EventContext context) {
    public Object onActivate() {
        // тип сущности
        objectClass = License.class;

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

        // проверяем права доступа
        SecurityUtils.getSubject().checkPermission(objectClass.getSimpleName() + ":read");
        return null;
    }

    //    /**
    //     * Выполняется в процессе формирования URL текущей страницы.
    //     */
    //    public Object onPassivate() {
    //        return this.objectClass.getSimpleName();
    //    }

    /**
     * Возвращает модель сущности, которая будет использована для показа списка.
     * Автоматически сформированная модель не годится,
     * потому что источник надписей надо заменить.
     */
    public GridBeanModel getModel() {
        return gridBeanModelSource.createDisplayModel(objectClass, new MsgAdapter());
    }

    /**
     * Параметры - часть URL страницы
     */
    public Object[] getRowContext() {
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

    /**
     * Адаптер для источника сообщений на нужном языке.
     * Соединяет интерфейс Messages и сервис ModelLabelSource
     */
    class MsgAdapter implements Messages {

        @Override
        public boolean contains(String key) {
            return true;
        }

        @Override
        public String get(String key) {

            String[] propertyName = key.split("\\.");
            if (propertyName.length >= 2) {
                try {
                    return modelLabels.getCellPropertyLabel(objectClass, capitalize(propertyName[1]), messages);
                } catch (java.lang.NullPointerException ex) {
                    if (messages.contains(propertyName[1])) {
                        return messages.get(propertyName[1]);
                    }
                }
            }
            if (messages.contains(key)) {
                return messages.get(key);
            }
            return key;
        }

        @Override
        public MessageFormatter getFormatter(String key) {
            return messages.getFormatter(key);
        }

        @Override
        public String format(String key, Object... args) {
            return messages.format(key, args);
        }

        /**
         * Returns a String which capitalizes the first letter of the string.
         */
        public String capitalize(String name) {
            if (name == null || name.length() == 0) {
                return name;
            }
            return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
        }
    }
}
