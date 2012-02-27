package ua.orion.web.license.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;
//import orion.tapestry.grid.lib.model.property.impl.GridPropertyModelLicenseRecord;
//import orion.tapestry.grid.lib.model.property.impl.GridPropertyModelOrgUnit;
//import orion.tapestry.grid.lib.model.property.impl.GridPropertyModelQualification;
import orion.tapestry.grid.lib.model.property.impl.GridPropertyModelLicenseRecord;
import ua.orion.cpu.core.eduprocplanning.entities.Qualification;
import ua.orion.cpu.core.licensing.entities.LicenseRecord;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;

/**
 *
 * @author dobro
 */
public class CpuGridJPALicenseModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("license", "ua.orion.web.license"));
    }

    /**
     * Конфигурация, соответствие между типом данных в Java и типом колонки в Grid
     * Add map Class => GridField type
     * @param configuration соответствие между типом данных в Java и типом колонки в Grid
     */
    public static void contributeGridPropertyModelSource(MappedConfiguration<String, Class> configuration) {
        // configuration.add(OrgUnit.class.getName(), GridPropertyModelOrgUnit.class);
        // configuration.add(Qualification.class.getName(), GridPropertyModelQualification.class);
        configuration.add(LicenseRecord.class.getName(), GridPropertyModelLicenseRecord.class);
    }
}
