package ua.orion.cpu.web.services;

import java.util.Enumeration;
import java.util.ResourceBundle;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.SubModule;
import ua.orion.cpu.core.eduprocplanning.services.EduProcPlanningIOCModule;
import ua.orion.cpu.core.licensing.services.LicensingIOCModule;
import ua.orion.cpu.core.orgunits.services.OrgUnitsIOCModule;
import ua.orion.cpu.core.persons.services.PersonsIOCModule;
import ua.orion.cpu.core.security.services.OrionSecurityIOCModule;
import ua.orion.cpu.core.services.OrionCpuIOCModule;
import ua.orion.cpu.web.eduprocplanning.services.EduProcPlanningWebIOCModule;
import ua.orion.cpu.web.licensing.services.LicensingWebIOCModule;
import ua.orion.cpu.web.orgunits.services.OrgUnitsWebIOCModule;
import ua.orion.cpu.web.persons.services.PersonsWebIOCModule;
import ua.orion.web.security.services.OrionSecurityWebIOCModule;

/**
 * Модуль конфигурирования IOC
 */
@SubModule({
    OrionSecurityIOCModule.class,
    OrionCpuIOCModule.class,
    LicensingIOCModule.class,
//    EduProcPlanningIOCModule.class,
    OrgUnitsIOCModule.class,
    PersonsIOCModule.class,
    OrionSecurityWebIOCModule.class,
    LicensingWebIOCModule.class,
//    EduProcPlanningWebIOCModule.class,
    OrgUnitsWebIOCModule.class,
    PersonsWebIOCModule.class
    
})
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
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            configuration.add(key, bundle.getString(key));
        }
    }

//    public static void contributeComponentEventLinkTransformer(
//            OrderedConfiguration<ComponentEventLinkTransformer> configuration)
//    {
//        configuration.addInstance("App", AppComponentEventLinkTransformer.class);
//    }
//    public static void contributeRealmSource(Configuration<Realm> configuration) {
//        ExtendedPropertiesRealm realm = new ExtendedPropertiesRealm("classpath:shiro-users.properties");
//        configuration.add(realm);
//    }
    
}
