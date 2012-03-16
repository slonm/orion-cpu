package ua.orion.cpu.core.security.services;

import javax.persistence.EntityManager;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.tynamo.security.core.SecurityCoreSymbols;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.security.AclActiveDirectoryRealm;
import ua.orion.cpu.core.security.OrionSecuritySymbols;

/**
 *
 * @author sl
 */
public class OrionSecurityIOCModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(ThreadRole.class, ThreadRoleImpl.class);
    }

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(OrionSecuritySymbols.SECURITY_LIB, "ua.orion.cpu.core.security"));
    }

    public static void contributeRealmSource(Configuration<Realm> config,
            @Symbol(SecurityCoreSymbols.ENABLED) final boolean enabled) {
        config.addInstance(StateAuthorizerRealm.class);
        if (enabled) {
            config.addInstance(AclActiveDirectoryRealm.class);
        }
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, String> configuration) {
        configuration.override(SecurityCoreSymbols.CONJUCTION_AUTHORITY, "true");
        configuration.add(OrionSecuritySymbols.EHCACHE_CONFIG, "/ehcache.xml");
    }
    
    @Match("EntityManager")
    public static void adviseEntityManagerWithSecurity(MethodAdviceReceiver receiver,
    SecurityAdvisor advisor){
        advisor.addAdvice(receiver);
    }
}
