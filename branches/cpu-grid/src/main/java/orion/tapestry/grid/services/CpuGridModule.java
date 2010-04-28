package orion.tapestry.grid.services;


import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;

/**
 * @author Gennadiy Dobrovolsky
 */
public class CpuGridModule {
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("grid", "orion.tapestry.grid"));
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add("grid/1.0", "orion/tapestry/grid");
    }
}


//import orion.tapestry.menu.lib.DefaultMenuLink;
//import orion.tapestry.menu.lib.IMenuLink;
//import orion.tapestry.menu.pages.Navigator;
//import org.apache.tapestry5.ioc.annotations.Inject;
//import org.apache.tapestry5.ioc.annotations.ServiceId;
//import org.apache.tapestry5.ioc.services.SymbolSource;
//import java.util.Map;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;