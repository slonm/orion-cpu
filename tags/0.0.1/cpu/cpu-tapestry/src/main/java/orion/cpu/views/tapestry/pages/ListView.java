package orion.cpu.views.tapestry.pages;

import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.views.tapestry.utils.EVTools;
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
    @Property
    private Object menudata;
    @Inject
    private Request request;
    @Inject
    private ComponentResources resources;
    @Inject
    private PageRenderLinkSource pageRenderLinkSource;
    @Inject
    @Symbol(SymbolConstants.START_PAGE_NAME)
    private String startPageName;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;

    /**
     * При закрытии страницы
     * @return
     * @author sl
     */
    public void onPassivate() {
        return;
    }

    /**
     * При открытии страницы
     * @param context
     * @return
     * @author sl
     */
    @SuppressWarnings("unchecked")
    public Object onActivate(EventContext context) {

        if (!isComponentEventRequst()) {
            try {
                Class<BaseEntity<?>> beanClass;
                //При необходимости выполняем переопределение пакета
                String pack = EVTools.getPackageByStringFromEventContext(context.get(String.class, 0));
                if (pack == null) {
                    beanClass = (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s.%s", rootPackage, entitiesPackage, context.get(String.class, 0)));
                } else {
                    beanClass = (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s", pack, context.get(String.class, 0)));
                }
                //Выполняем очистку сохранённых данных загруженной страницы,
                //если вызывается другая сущность, отображаемая в этой странице
                if (!beanClass.equals(getEntityClass()) && getEntityClass() != null) {
                    resources.discardPersistentFieldChanges();
                    Link link = pageRenderLinkSource.createPageRenderLinkWithContext(ListView.class, context);
                    for (String s : request.getParameterNames()) {
                        link.addParameter(s, request.getParameter(s));
                    }
                    return link;
                }
                setEntityClass(beanClass);
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to root page");
                return "";
            }
        }
        getAuthorizer().checkSearch(getEntityClass());
        title = messages.get("reflect." + getEntityClass().getName());
        menudata = request.getParameter("menupath");
        return null;
    }

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
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
                //При необходимости выполняем переопределение пакета
                String pack = EVTools.getPackageByStringFromEventContext(input.getContext()[0].toString());
                if (pack == null) {
                    return (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s.%s", rootPackage, entitiesPackage, input.getContext()[0]));
                } else {
                    return (Class<BaseEntity<?>>) Class.forName(String.format("%s.%s", pack, input.getContext()[0]));
                }
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
