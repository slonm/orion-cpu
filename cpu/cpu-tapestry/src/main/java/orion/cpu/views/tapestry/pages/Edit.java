package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.tapestrycrud.hibernatevalidator.base.HibernateValidatorBaseEditPage;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.Coercion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orion.cpu.baseentities.BaseEntity;
import orion.tapestry.menu.lib.IMenuLink;

/**
 * Универсальная страница редактирования {@link BaseEntity}
 * @author sl
 */
@SuppressWarnings("unused")
public class Edit extends HibernateValidatorBaseEditPage<BaseEntity<?>, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(Edit.class);
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

    @Override
    public Object onPassivate() {
        if (getObject() != null) {
            return getActivationContextEncoder(getEntityClass()).toActivationContext(getObject());
        } else if (isInitiated()) {
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
    @Override
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
        setEntityClass(beanClass);

        if (context.getCount() == 1) {
            // FIXME Очищает Message при добавлении новой записи. Нужно еще
            // сделать очистку при редактироании
            setMessage("");
            checkStoreTypeAccess();
        } else {
            checkUpdateTypeAccess();
        }
        final BaseEntity<?> activationContextObject = getActivationContextEncoder(
                getEntityClass()).toObject(context);
        if (activationContextObject != null) {
            checkUpdateObjectAccess(activationContextObject);
        }

        setObject(activationContextObject);
        title = messages.get("reflect." + beanClass.getName());
        action = getObject() == null ? action : messages.get("button.save");

        return null;
    }

    public String getListPageURL() {
        return getTapestryCrudModuleService().getListPageURL(getEntityClass());
    }

    public String getListPageURLContext() {
        return BaseEntity.getFullClassName(getEntityClass());
    }

    public Object getContext() {
        return getActivationContextEncoder(getEntityClass()).toActivationContext(getObject());
    }

    public boolean getCreate() {
        return getAuthorizer().canStore(getEntityClass());
    }

    public boolean getListView() {
        return getAuthorizer().canRead(getEntityClass());
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
