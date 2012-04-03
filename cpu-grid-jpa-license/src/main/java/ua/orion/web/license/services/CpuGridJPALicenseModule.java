package ua.orion.web.license.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.services.LibraryMapping;
/**
 *
 * @author dobro
 */
public class CpuGridJPALicenseModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("license", "ua.orion.web.license"));
    }

}
