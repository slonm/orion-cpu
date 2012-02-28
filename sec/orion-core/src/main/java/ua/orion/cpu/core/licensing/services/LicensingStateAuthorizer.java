/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.licensing.services;

import java.util.Arrays;
import javax.inject.Inject;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.licensing.entities.License;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.licensing.entities.LicenseState;
import ua.orion.cpu.core.security.services.StateAuthorizer;

/**
 *
 * @author slobodyanuk
 */
public class LicensingStateAuthorizer implements StateAuthorizer {

    @Inject
    private EntityService es;
    @Inject
    private LicensingService ls;

    @Override
    public boolean isForbid(String permission) {
        String[] parts = permission.split(":");
        if (parts.length == 2) {
            if ("insert".equals(parts[1]) && "License".equals(parts[0])) {
                return ls.existsNewStateLicense();
            }
        } else if (parts.length == 3) {
            if (Arrays.asList("update", "delete").contains(parts[1])) {
                switch (parts[0]) {
                    case "License":
                        License license = es.find(License.class, parts[2]);
                        if (LicenseState.NEW!=license.getLicenseState()) {
                            return true;
                        }
                        break;
                    case "LicenseRecord":
                        LicenseRecord licenseRecord = es.find(LicenseRecord.class, parts[2]);
                        if (LicenseState.NEW!=licenseRecord.getLicense().getLicenseState()) {
                            return true;
                        }
                        break;
                }
            }
        }
        return false;
    }
}
