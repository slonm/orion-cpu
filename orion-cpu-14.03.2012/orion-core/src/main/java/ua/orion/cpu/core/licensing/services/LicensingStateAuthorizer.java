package ua.orion.cpu.core.licensing.services;

import java.util.Arrays;
import javax.inject.Inject;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.License;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.licensing.entities.LicenseState;
import ua.orion.cpu.core.security.OrionWildcardPermission;
import ua.orion.cpu.core.security.services.StateAuthorizer;

/**
 * Логика проверки допустимости выполнения операций с объектами типа License и 
 * LicenseRecord
 * @author slobodyanuk
 */
public class LicensingStateAuthorizer implements StateAuthorizer {

    @Inject
    private EntityService es;
    @Inject
    private LicensingService ls;

    @Override
    public boolean isForbid(String permission) {
        try {
            OrionWildcardPermission owp = new OrionWildcardPermission(permission);
            if ("insert".equals(owp.getAction()) && "license".equals(owp.getDomain())) {
                return ls.existsNewStateLicense();
            } else if (Arrays.asList("update", "delete").contains(owp.getAction())) {
                switch (owp.getDomain()) {
                    case "license":
                        License license = es.find(License.class, owp.getInstance());
                        if (LicenseState.NEW != license.getLicenseState()) {
                            return true;
                        }
                        break;
                    case "licenserecord":
                        LicenseRecord licenseRecord = es.find(LicenseRecord.class, owp.getInstance());
                        if (LicenseState.NEW != licenseRecord.getLicense().getLicenseState()) {
                            return true;
                        }
                        break;
                }
            }
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}
