package ua.orion.cpu.core.licensing.stringvalues;

import java.text.DateFormat;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import ua.orion.core.services.StringValueProvider;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;

/**
 *
 * @author slobodyanuk
 */
public class LicenseRecordStringValueProvider implements StringValueProvider<LicenseRecord> {

    @Inject
    private ThreadLocale thLocale;

    @Override
    public String getStringValue(LicenseRecord e) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, thLocale.getLocale());
        StringBuilder sb = new StringBuilder();
        sb.append(e.getCode()).append(" - ");
        sb.append(e.getSpeciality() == null ? e.getTrainingDirection().getName() : e.getSpeciality().getName()).append(" (");
        sb.append(e.getLicenseRecordGroup().getName()).append(") ");
        sb.append(dateFormat.format(e.getTermination().getTime()));
        return sb.toString();
    }
}
