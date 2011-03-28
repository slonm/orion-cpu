package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
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
 * Универсальная страница со списком {@link BaseEntity}
 * @author sl
 */
@SuppressWarnings("unused")
public class ListView extends BaseListPage<BaseEntity<?>> {

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
            //FIXME entitiesPackage не один, нужно искать во всех
            beanClass = (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s.%s", rootPackage, entitiesPackage,
                    context.get(String.class, 0)));
        } catch (Exception ex) {
            LOG.debug("Invalid activation context. Redirect to root page");
            return "";
        }
        setEntityClass(beanClass);
        getAuthorizer().checkSearch(getEntityClass());
        title = messages.get("reflect." + beanClass.getName());
        return null;
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
