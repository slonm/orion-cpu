package ua.orion.cpu.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;
import org.tynamo.jpa.Ejb3HibernateSymbols;
import ua.orion.core.utils.IOCUtils;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.licensing.services.LicensingIOCModule;
import ua.orion.cpu.core.orgunits.services.OrgUnitsIOCModule;
import ua.orion.cpu.core.security.services.OrionSecurityIOCModule;
import ua.orion.cpu.core.services.OrionCpuIOCModule;

/**
 * Модуль конфигурирования IOC
 */
@SubModule({LicensingIOCModule.class, OrionSecurityIOCModule.class, OrgUnitsIOCModule.class, OrionCpuIOCModule.class})
public class AppModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(IndexPageRenderLinkTransformer.class);
    }

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
        configuration.add(SymbolConstants.COMPRESS_WHITESPACE, SymbolConstants.PRODUCTION_MODE_VALUE);
        //Заполнение базы данных тестовыми данными
        configuration.add(OrionCPUSymbols.TEST_DATA, "true");
        configuration.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, "false");
//        configuration.add(SymbolConstants.BLACKBIRD_ENABLED, "true");
        configuration.add(Ejb3HibernateSymbols.CREATE_SCHEMA_STATEMENT, "CREATE SCHEMA %s");
    }

    public static void contributePageRenderLinkTransformer(OrderedConfiguration<PageRenderLinkTransformer> configuration,
            IndexPageRenderLinkTransformer lTranformer) {
        configuration.add("IndexPage", lTranformer);
    }
//    public static void contributeComponentEventLinkTransformer(
//            OrderedConfiguration<ComponentEventLinkTransformer> configuration)
//    {
//        configuration.addInstance("App", AppComponentEventLinkTransformer.class);
//    }

    @Match("ComponentClassResolver")
    public static void adviseComponentClassResolverWithForeigIndex(MethodAdviceReceiver receiver) {

        MethodAdvice advice = new MethodAdvice() {

            @Override
            public void advise(Invocation invocation) {
                if ("".equals(invocation.getParameter(0).toString())) {
                    invocation.override(0, "ori/index");
                }
                invocation.proceed();
            }
        };
        receiver.adviseMethod(IOCUtils.getMethod(ComponentClassResolver.class, "canonicalizePageName", String.class), advice);
    }

}
