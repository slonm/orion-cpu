package ua.orion.cpu.web.eduprocplanning.pages;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

/**
 *
 * @author sl
 */
@SuppressWarnings("unused")
public class EduPlans {
    //---Services---

    @Inject
    private Request request;
    @Inject
    private Logger LOG;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;

    public boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    public Class<?> getObjectClass() {
        return ua.orion.cpu.core.eduprocplanning.entities.EduPlan.class;
    }

    public Object onActivate(EventContext context) {
        if (!isComponentEventRequst()) {
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

}
