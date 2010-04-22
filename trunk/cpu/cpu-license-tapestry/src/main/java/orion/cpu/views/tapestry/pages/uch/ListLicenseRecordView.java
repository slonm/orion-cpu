package orion.cpu.views.tapestry.pages.uch;

import orion.cpu.views.tapestry.pages.*;
import orion.cpu.entities.uch.*;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import java.util.List;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.views.tapestry.pages.ErrorReport;
import orion.cpu.views.tapestry.pages.Login;

/**
 *
 * @author sl
 */
public class ListLicenseRecordView extends BaseListPage<LicenseRecordView, Integer> {

    @Inject
    private ControllerSource controllerSource;
    @Property
    private String license;
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
        if (!getAuthorizer().canSearch(getEntityClass())) {
            if (user != null) {
                return errorReport.getErrorReportLink(ErrorReport.ACCESS_DENIED);
            } else {
                return login.setRedirectURL();
            }
        }
        List<License> licList = controllerSource.get(License.class).findAll();
        if(licList.size()==0){
            //TODO сделать нормальное сообщение
            return errorReport.getErrorReportLink(ErrorReport.ACCESS_DENIED);
        }
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
