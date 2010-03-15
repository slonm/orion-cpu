package test.menu.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import orion.tapestry.menu.lib.DefaultLinkCreator;
import orion.tapestry.menu.lib.LinkCreator;
import orion.tapestry.menu.pages.Navigator;
import orion.tapestry.menu.services.DefaultLinkCreatorFactory;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
//Перечень дополнительных модулей
@SubModule({orion.tapestry.menu.services.CpuMenuModule.class})
public class TstModule {

    /**
     * Add menu item to configuration
     * @param configuration
     */
    public static void contributeCpuMenu(MappedConfiguration<String, LinkCreator> configuration,
            DefaultLinkCreatorFactory linkFactory) {

        String path;

        path = "Start";
        configuration.add(path, linkFactory.create(path));

        path = "Start>abo";
        configuration.add(path, linkFactory.create(path));

        path = "Start>abo>persons";
        configuration.add(path, linkFactory.create(path));

        path = "Start>abo>speciality";
        configuration.add(path, linkFactory.create(path));

        path = "Start>ok";
        configuration.add(path, linkFactory.create(path));

        path = "Start>ok>staff";
        configuration.add(path, linkFactory.create(path));

        path = "Start>ok>persons";
        configuration.add(path, linkFactory.create(path));
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "ru,uk,en");
    }
}
