package orion.cpu.views.tapestry.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.*;
import orion.cpu.services.CoreIOCModule;

/**
 * Модуль конфигурирования IOC
 */
public class AppModule {

    /**
     * Регистрация и конфигурирование опций.
     * @param configuration
     */
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).

//FIXME app_ua.properties не читает
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "uk,ru,en");

        // The factory default is true but during the early stages of an application
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions.
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
        //-Djava.util.logging.config.file="logging.properties"
        //configuration.add("java.util.logging.config.file", "logging.properties");
        configuration.add("tapestry.compress-whitespace", "false");
        //Заполнение базы данных тестовыми данными
        configuration.add(CoreIOCModule.FILL_TEST_DATA, "true");
        configuration.add("tapestry.default-stylesheet", "classpath:/default.css");
    }

}
