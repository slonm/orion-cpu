package ua.orion.cpu.web.services;

import java.util.Enumeration;
import java.util.ResourceBundle;
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
        ResourceBundle bundle = ResourceBundle.getBundle("Cpu");
        Enumeration<String> e = bundle.getKeys();
        while (e.hasMoreElements()){
            String key=e.nextElement();
            configuration.add(key, bundle.getString(key));
        }
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

//    public static void contributeRealmSource(Configuration<Realm> configuration) {
//        ExtendedPropertiesRealm realm = new ExtendedPropertiesRealm("classpath:shiro-users.properties");
//        configuration.add(realm);
//    }

}
