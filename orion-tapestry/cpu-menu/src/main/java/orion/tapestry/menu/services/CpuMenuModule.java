/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.services;

import java.util.Map;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.LibraryMapping;
import orion.tapestry.menu.lib.IMenuLink;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class CpuMenuModule {

    /**
     * CpuMenu Service builder
     * @param configuration  registry of menu items
     * @param ss symbol source to get navigator page
     * @return CpuMenu object
     */
    @ServiceId("CpuMenu")
    public static CpuMenu buildCpuMenu(Map<String, IMenuLink> configuration,  @Inject SymbolSource ss ) {
        return new CpuMenuImpl(configuration);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("menu", "orion.tapestry.menu"));
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add("menu/1.0", "orion/tapestry/menu");
    }

}
