package ua.orion.cpu.security.services;

import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.tynamo.jpa.Ejb3HibernateConfigurer;

/**
 *
 * @author sl
 */
public class OrionSecurityIOCModule {
    public static void contributeEjb3HibernateEntityManagerSource(OrderedConfiguration<Ejb3HibernateConfigurer> config) {
        config.addInstance("AuthorityListener", HibernateAuthorityEventListener.class);
    }
    
    public static void contributeRealmSource(OrderedConfiguration<Realm> config) {
        config.addInstance("OrionAuthorization", OrionAuthorizationRealm.class);
    }
}
