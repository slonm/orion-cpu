package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.baseentities.BaseEntity;
import orion.tapestry.menu.lib.IMenuLink;

/**
 * Универсальная страница со списком {@link BaseEntity}
 * @author sl
 */
@SuppressWarnings("unused")
public class ListView extends BaseListPage<BaseEntity<?>, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(ListView.class);
    /**
     * Заголовок страницы
     */
    @Property
    private String title;
    @Inject
    private Messages messages;
    @Inject
    @Symbol("orion.root-package")
    private String rootPackage;
    @Inject
    @Symbol("orion.entities-package")
    private String entitiesPackage;
    @InjectPage
    private ErrorReport errorReport;
    @InjectPage
    private Login login;
    @SessionState(create = false)
    private User user;

    /**
     * При закрытии страницы
     * @return
     * @author sl
     */
    public Object onPassivate() {
        if (getObject() != null) {
            return getActivationContextEncoder(getEntityClass()).
                    toActivationContext(getObject());
        } else if (isInitiated()) {
            return BaseEntity.getFullClassName(getEntityClass());
        }
        return null;
    }

    /**
     * При открытии страницы
     * @param context
     * @return 
     * @author sl
     */
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {
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
        if (!getAuthorizer().canSearch(getEntityClass())) {
            if (user!=null) {
                return errorReport.getErrorReportLink(ErrorReport.ACCESS_DENIED);
            } else {
                return login.setRedirectURL();
            }
        }
        title = messages.get("reflect." + beanClass.getName());
        return null;
    }

    @Override
    public void onActivate() {
    }

    public String getEditPageURL() {
        return getTapestryCrudModuleService().getEditPageURL(getEntityClass());
    }

    public String getEditPageURLContext() {
        return BaseEntity.getFullClassName(getEntityClass());
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
                assert input.getPageClass().equals(ListView.class);
                assert input.getContext().length > 0;
                return (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s.%s",
                        rootPackage, entitiesPackage, input.getContext()[0]));
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
