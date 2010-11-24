package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.tapestrycrud.CrudEditPage;
//import br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins.HibernateValidatorMixin;
import br.com.arsmachina.tapestrycrud.mixins.CrudEditPageMixin;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orion.cpu.baseentities.BaseEntity;
import orion.tapestry.menu.lib.IMenuLink;

/**
 * Универсальная страница редактирования {@link BaseEntity}
 * @author sl
 */
@SuppressWarnings("unused")
public class Edit implements CrudEditPage<BaseEntity<?>, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(Edit.class);

    @Component
    private Form form;

    @Mixin
    private CrudEditPageMixin<BaseEntity<?>, Integer> crudEditPageMixin;

    /**
     * Заголовок страницы
     */
    @Property
    private String title;
    /**
     * Подпись тип действия. Редактирование/добавление
     */
    @Property
    private String action;
    @Inject
    private Messages messages;
    @Inject
    @Symbol("orion.root-package")
    private String rootPackage;
    @Inject
    @Symbol("orion.entities-package")
    private String entitiesPackage;
    @Property
    private Object menudata;
    @Inject
    private Request request;
    @Inject
    private ActivationContextEncoderSource acEncoderSource;
    @Inject
    private TapestryCrudModuleService tCrudModuleService;

    public Object onPassivate() {
        if (getObject() != null) {
            return acEncoderSource.get(getEntityClass()).toActivationContext(getObject());
        } else if (crudEditPageMixin.isInitiated()) {
            return BaseEntity.getFullClassName(getEntityClass());
        }
        return null;
    }

    /**
     * При открытии страницы
     *
     * @param context
     * @return 
     * @author sl
     */
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {
        action = messages.get("button.create");
        Class<BaseEntity<?>> beanClass;
        try {
            assert context.getCount() > 0;
            beanClass = (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s.%s", rootPackage, entitiesPackage,
                    context.get(String.class, 0)));
        } catch (Exception ex) {
            LOG.debug("Invalid activation context. Redirect to root page");
            return "";
        }
        crudEditPageMixin.setEntityClass(beanClass);

        if (context.getCount() == 1) {
            // FIXME Очищает Message при добавлении новой записи. Нужно еще
            // сделать очистку при редактироании
            setMessage("");
            crudEditPageMixin.checkStoreTypeAccess();
        } else {
            crudEditPageMixin.checkUpdateTypeAccess();
        }
        final BaseEntity<?> activationContextObject = acEncoderSource.get(
                getEntityClass()).toObject(context);
        if (activationContextObject != null) {
            crudEditPageMixin.checkUpdateObjectAccess(activationContextObject);
        }

        setObject(activationContextObject);
        title = messages.get("reflect." + beanClass.getName());
        action = getObject() == null ? action : messages.get("button.save");
        menudata = request.getParameter("menupath");
        return null;
    }

    public String getListPageURL() {
        return tCrudModuleService.getListPageURL(getEntityClass());
    }

    public String getListPageURLContext() {
        return BaseEntity.getFullClassName(getEntityClass());
    }

    public Object getContext() {
        return acEncoderSource.get(getEntityClass()).toActivationContext(getObject());
    }

    public boolean getCreate() {
        return crudEditPageMixin.getAuthorizer().canStore(getEntityClass());
    }

    public boolean getListView() {
        return crudEditPageMixin.getAuthorizer().canRead(getEntityClass());
    }

    @Override
    public BaseEntity<?> getObject() {
        return crudEditPageMixin.getObject();
    }

    @Override
    public void setObject(BaseEntity<?> object) {
        crudEditPageMixin.setObject(object);
    }

    @Override
    public Object getFormZone() {
        return crudEditPageMixin.getFormZone();
    }

    @Override
    public String getMessage() {
        return crudEditPageMixin.getMessage();
    }

    @Override
    public void setMessage(String message) {
        crudEditPageMixin.setMessage(message);
    }

    @Override
    public Class<BaseEntity<?>> getEntityClass() {
        return crudEditPageMixin.getEntityClass();
    }

    @Override
    public Class<?> getPrimaryKeyClass() {
        return Integer.class;
    }

    @Override
    public String getZone() {
        return crudEditPageMixin.getZone();
    }

    @Override
    public BeanModel<BaseEntity<?>> getBeanModel() {
        return crudEditPageMixin.getBeanModel();
    }

    @Override
    public Form getForm() {
        return form;
    }

    public static class MetaLinkCoercion implements Coercion<IMenuLink, Class<BaseEntity<?>>> {

        @Inject
        @Symbol("orion.root-package")
        private String rootPackage;
        @Inject
        @Symbol("orion.entities-package")
        private String entitiesPackage;

        @Override
        public Class<BaseEntity<?>> coerce(IMenuLink input) {
            try {
                assert input.getPageClass().equals(Edit.class);
                assert input.getContext().length > 0;
                return (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s.%s",
                        rootPackage, entitiesPackage, input.getContext()[0]));
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
