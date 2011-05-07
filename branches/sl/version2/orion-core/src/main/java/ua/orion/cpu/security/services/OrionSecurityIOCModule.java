package ua.orion.cpu.security.services;

import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.tynamo.jpa.Ejb3HibernateConfigurer;
import ua.orion.core.ModelLibraryInfo;

/**
 *
 * @author sl
 */
public class OrionSecurityIOCModule {
    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("Security", "ua.orion.cpu.security"));
    }
    
    public static void contributeEjb3HibernateEntityManagerSource(OrderedConfiguration<Ejb3HibernateConfigurer> config) {
        //config.addInstance("AuthorityListener", HibernateAuthorityEventListener.class);
    }
    
    public static void contributeRealmSource(Configuration<Realm> config) {
        config.addInstance(OrionAuthorizationRealm.class);
    }
}
