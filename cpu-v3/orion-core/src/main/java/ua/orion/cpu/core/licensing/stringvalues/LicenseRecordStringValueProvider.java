package ua.orion.cpu.core.licensing.stringvalues;

import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.core.services.StringValueProvider;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;

/**
 *
 * @author slobodyanuk
 */
public class LicenseRecordStringValueProvider implements StringValueProvider<LicenseRecord> {

    @Inject
    private StringValueProvider stringValueProvider;

    @Override
    public String getStringValue(LicenseRecord e) {
        if (e == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(e.getCode()).append(" - ");
        sb.append(e.getSpeciality() == null ? e.getTrainingDirection().getName() : e.getSpeciality().getName()).append(" (");
        sb.append(e.getLicenseRecordGroup().getName()).append(") ");
//        sb.append(stringValueProvider.getStringValue(e.getTermination()));
        return sb.toString();
    }
}
