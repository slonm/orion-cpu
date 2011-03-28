package orion.cpu.views.tapestry.pages.uch;

import orion.cpu.entities.uch.*;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import java.util.List;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import orion.cpu.baseentities.BaseEntity;

/**
 *
 * @author sl
 */
public class ListLicenseRecordView extends BaseListPage<LicenseRecordView, Integer> {

    public static final String MENU_PATH = "Start>License>LicenseRecordView";
    @Inject
    private ControllerSource controllerSource;
    @Property
    private String license;
    @Inject
    @Property
    private Request request;

    public String getMENU_PATH(){
        return MENU_PATH;
    }
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
        getAuthorizer().checkSearch(getEntityClass());
        List<License> licList = controllerSource.get(License.class).findAll();
        license = licList.get(0).toString();
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
}
