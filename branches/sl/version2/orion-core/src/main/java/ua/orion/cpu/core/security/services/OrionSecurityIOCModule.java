package ua.orion.cpu.core.security.services;

import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.tynamo.jpa.Ejb3HibernateConfigurer;
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

    public static void contributeEjb3HibernateEntityManagerSource(OrderedConfiguration<Ejb3HibernateConfigurer> config) {
        //config.addInstance("AuthorityListener", HibernateAuthorityEventListener.class);
    }

    public static void contributeRealmSource(Configuration<Realm> config,
            EntityManager em,
            @Symbol("ldap.system-username") String systemUsername,
            @Symbol("ldap.system-password") String systemPassword,
            @Symbol("ldap.search-base") String searchBase,
            @Symbol("ldap.url") String url,
            @Symbol("ldap.principal-suffix") String principalSuffix) {
        AclActiveDirectoryRealm realm = new AclActiveDirectoryRealm(em);
        realm.setSystemUsername(systemUsername);
        realm.setSystemPassword(systemPassword);
        realm.setSearchBase(searchBase);
        realm.setUrl(url);
        realm.setPrincipalSuffix(principalSuffix);
        config.add(realm);
    }
}
