package ua.orion.cpu.web.licensing.pages;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import ua.orion.cpu.core.licensing.services.LicensingService;
import ua.orion.web.services.RequestInfo;

/**
 * Страница, которая предоставляет CRUD лицензий
 * @author slobodyanuk
 */
@SuppressWarnings("unused")
public class Licenses {
    //---Services---

    @Inject
    private RequestInfo info;
    @Inject
    private Logger LOG;
    @Inject
    private LicensingService ls;

    public Object onActivate(EventContext context) {
        if (!info.isComponentEventRequest()) {
            //Страница не должна получать контекста при запросе страницы
            try {
                if (context.getCount() != 0) {
                    throw new RuntimeException();
                }
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
        }
        return null;
    }

    void onAfterPersistFromCrud(ua.orion.cpu.core.licensing.entities.License license) {
        ls.cloneLicenseRecordsFromForced(license);
    }
}
