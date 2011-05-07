/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.services;

import java.util.Map;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.LibraryMapping;
import ua.orion.tapestry.menu.lib.IMenuLink;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class OrionMenuIOCModule {

    /**
     * CpuMenu Service builder
     * @param configuration  registry of menu items
     * @param ss symbol source to get navigator page
     * @return CpuMenu object
     */
    public static OrionMenuService buildOrionMenuService(Map<String, IMenuLink> configuration,  @Inject SymbolSource ss ) {
        return new OrionMenuServiceImpl(configuration);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("menu", "ua.orion.tapestry.menu"));
    }
}
