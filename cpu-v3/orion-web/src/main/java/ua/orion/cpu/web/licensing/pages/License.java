package ua.orion.cpu.web.licensing.pages;

import javax.persistence.criteria.*;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.web.AdditionalConstraintsApplier;
import ua.orion.web.services.RequestInfo;
import ua.orion.web.services.TapestryDataSource;

/**
 * Страница, которая предоставляет CRUD для простых сущностей
 * @author slobodyanuk
 */
@SuppressWarnings("unused")
public class License {
    //---Services---

    @Inject
    private RequestInfo info;
    @Inject
    private Logger LOG;
    @Inject
    @Property(write = false)
    private EntityService entityService;
    @Inject
    private TapestryDataSource dataSource;
    //---Locals---
    @Property
    @Persist
    private ua.orion.cpu.core.licensing.entities.License object;

    /**
     * Задано явно для возможности вызова из других классов
     */
    public Class<?> getLicenseRecordClass() {
        return LicenseRecord.class;
    }

    public GridDataSource getSource() {
        return dataSource.getGridDataSource(LicenseRecord.class, new AdditionalConstraintsApplier<LicenseRecord>(){

            @Override
            public void applyAdditionalConstraints(CriteriaQuery<LicenseRecord> criteria, 
            Root<LicenseRecord> root, CriteriaBuilder builder) {
                criteria.where(builder.equal(root.get("license"), object));
            }
        });
    }

    public Object onActivate(EventContext context) {
        if (!info.isComponentEventRequest()) {
            try {
                if (context.getCount() != 1) {
                    throw new RuntimeException();
                }
                object = entityService.find(ua.orion.cpu.core.licensing.entities.License.class, context.get(String.class, 0));
                object.getId(); //throw exception if object is null
            } catch (Exception ex) {
                LOG.debug("Invalid activation context. Redirect to start page");
                return "";
            }
            SecurityUtils.getSubject().checkPermission("License:read:" + object.getId());
        }
        return null;
    }
}
