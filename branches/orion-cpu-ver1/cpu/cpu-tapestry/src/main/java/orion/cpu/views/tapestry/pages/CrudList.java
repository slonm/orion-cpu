package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.authentication.service.UserService;
import br.com.arsmachina.authorization.Authorizer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.Request;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;
import orion.cpu.entities.grid.GridSetting;
import orion.tapestry.grid.lib.model.GridModelInterface;
import orion.tapestry.grid.lib.model.GridModelLocalizator;

import orion.tapestry.grid.lib.savedsettings.*;
import orion.tapestry.services.FieldLabelSource;

/**
 *
 * @author dobro
 */
public class CrudList extends orion.cpu.views.tapestry.pages.crud.CrudList {

    /**
     * configuration: root package
     * корневой пакет Tapestry
     */
    @Inject
    @Symbol("orion.root-package")
    private String rootPackage;
    /**
     * configuration: package where entities are located
     * пакет, в котором хранятся сущности для БД
     */
    @Inject
    @Symbol("orion.entities-package")
    private String entitiesPackage;
    /**
     * Сервис, который хранит информацию о текущем пользователе.
     */
    @Inject
    private UserService userService;
    /**
     * Рermission checker
     */
    @Inject
    private Authorizer authorizer;
    /**
     *  interface messages
     */
    @Inject
    private Messages messages;
    /**
     * tolerant messages retrieval
     */
    private 
    @Inject
    FieldLabelSource fieldLabelSource;
    /**
     *  navigation menu
     */
    @Property
    private Object menudata;
    /**
     * HTTP request
     */
    @Inject
    private Request request;
    /**
     * Соединение с БД
     */
    @Inject
    private HibernateSessionManager sm;
    /**
     * Сессия - подключение к БД
     */
    private Session session;
    /**
     * Обьект, отвечающий за сохранение и извлечение
     * настроек табличного просмотра.
     */
    private IGridSettingStore gridSettingStore;

    /**
     * Создаёт класс по имени.
     * Наличие метода позволяет организовать создание класса по сокращённому имени.
     * @param entityClassName полное или частичное имя класса
     * @return класс сущности
     */
    @Override
    public Class entityClassForName(String entityClassName) throws ClassNotFoundException {
        //LOG.error("entityClassForName: "+String.format("%s.%s", this.rootPackage, this.entitiesPackage));
        if (entityClassName.startsWith(String.format("%s.%s", this.rootPackage, this.entitiesPackage))) {
            return Class.forName(entityClassName);
        } else {
            return Class.forName(String.format("%s.%s.%s", this.rootPackage, this.entitiesPackage, entityClassName));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object onActivate(EventContext context) {
        Object returnMe = super.onActivate(context);
        if (this.getEntityClass() == null) {
            LOG.error("Activation Error: entity class not found");
            return "";
        }

        // check permissions
        this.authorizer.checkSearch(this.getEntityClass());

        // set page title
        this.setTitle(messages.get("reflect." + this.getEntityClassName()));

        menudata = request.getParameter("menupath");
        if (menudata == null || menudata.toString().length() == 0) {
            menudata = "Start";
        }

        // Create hibernate session
        this.session = sm.getSession();

        // create object to store/load grid setting
        this.gridSettingStore = new HibernateGridSettingStore();

        return returnMe;
    }

    /**
     * interface to saved grid settings
     * @author dobro
     */
    public class HibernateGridSettingStore implements IGridSettingStore {

        @Override
        public String getSavedSetting(Long id) {
            Query query = session.createQuery("from " + GridSetting.class.getSimpleName() + " where id=" + id);
            List result = query.list();
            if (!result.isEmpty()) {
                return ((GridSetting) result.get(0)).getSettingJSON();
            } else {
                return null;
            }
        }

        @Override
        public boolean deleteSavedSetting(Long id) {
            Query query = session.createQuery("from " + GridSetting.class.getSimpleName() + " where id=" + id);
            List result = query.list();
            if (!result.isEmpty()) {
                Object entity = result.get(0);
                session.delete(entity);
                sm.commit();
                return true;
            }
            return false;
        }

        @Override
        public String getSavedSettingListJSON() {
            Query query = session.createQuery("from " + GridSetting.class.getSimpleName() + " where userId=" + userService.getUser().getId());
            List result = query.list();
            if (!result.isEmpty()) {
                JSONArray arrJSON = new JSONArray();
                for (Object rec : result) {
                    GridSetting r = (GridSetting) rec;
                    JSONObject recJSON = new JSONObject();
                    try {
                        recJSON.append("id", r.getId());
                        recJSON.append("label", r.getLabel());
                        arrJSON.put(recJSON);
                    } catch (JSONException ex) {
                        Logger.getLogger(CrudList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return arrJSON.toString();
            }
            return "[]";
        }

        @Override
        public boolean saveSetting(String label, String setting) {

            GridSetting entity = new GridSetting();
            entity.setLabel(label);
            entity.setSettingJSON(setting);
            entity.setUserId(new Long(userService.getUser().getId()));

            session.beginTransaction();
            session.saveOrUpdate(entity);
            sm.commit();
            return true;
        }
    }

    /**
     * Обьект, отвечающий за сохранение и извлечение
     * настроек табличного просмотра.
     */
    public IGridSettingStore getGridSettingStore() {
        return this.gridSettingStore;
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    /**
     * @return Модель данных для grid
     */
    @Override
    public GridModelInterface getGridModel() {
        GridModelInterface model = super.getGridModel();

        // декоратор для модели
        // заменяет подписи для
        GridModelLocalizator localizator = new GridModelLocalizator() {
            @Override
            protected String getLabel(String uid) {
                String propertyName=uid.replaceFirst("^(\\w+\\.)+", "");
                System.out.println(getEntityClass()+" ==== "+propertyName +" === "+ messages);
                //return fieldLabelSource.get(getEntityClass(), propertyName, messages);
                return "msg:" + uid;
            }
        };
        return localizator.localize(model);
    }
}
