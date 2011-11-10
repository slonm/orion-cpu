package ua.orion.cpu.web.licensing.pages;

import javax.persistence.criteria.*;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import org.tynamo.jpa.internal.JPAGridDataSource;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * @author slobodyanuk
 */
@SuppressWarnings("unused")
public class License {
    //---Services---
    @Inject
    private Request request;
    @Inject
    private Logger LOG;
    @Inject
    private ComponentEventLinkEncoder componentEventLinkEncoder;
    @Inject
    @Property(write = false)
    private EntityService entityService;
    //---Locals---
    @Property
    @Persist
    private ua.orion.cpu.core.licensing.entities.License object;

    private boolean isComponentEventRequst() {
        return componentEventLinkEncoder.decodeComponentEventRequest(request) != null;
    }

    /**
     * Задано явно для возможности вызова из других классов
     */
    public Class<?> getLicenseRecordClass() {
        return LicenseRecord.class;
    }

    public GridDataSource getSource() {
        return new JPAGridDataSource<LicenseRecord>(entityService.getEntityManager(), LicenseRecord.class) {

            @Override
            protected Expression<Boolean> additionalConstraints(CriteriaBuilder cb, Root<LicenseRecord> root) {
                return cb.equal(root.get("license"), object);
            }
        };
    }

    public Object onActivate(EventContext context) {
        if (!isComponentEventRequst()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                object = context.get(ua.orion.cpu.core.licensing.entities.License.class, 0);
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            SecurityUtils.getSubject().checkPermission("License:read:" + object.getId());
        }
        return null;
    }
}
