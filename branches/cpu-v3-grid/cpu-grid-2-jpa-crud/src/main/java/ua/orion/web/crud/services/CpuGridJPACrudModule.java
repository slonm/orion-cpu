package ua.orion.web.crud.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.services.LibraryMapping;

/**
 *
 * @author dobro
 */
public class CpuGridJPACrudModule {

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("crud", "ua.orion.web.crud"));
    }


}
