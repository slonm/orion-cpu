package ua.orion.cpu.core.security.services;

import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.tynamo.jpa.Ejb3HibernateConfigurer;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.security.AclActiveDirectoryRealm;
import ua.orion.cpu.core.security.OrionSecuritySymbols;

/**
 *
 * @author sl
 */
public class OrionSecurityIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(OrionSecuritySymbols.SECURITY_LIB, "ua.orion.cpu.core.security"));
    }

    public static void contributeEjb3HibernateEntityManagerSource(OrderedConfiguration<Ejb3HibernateConfigurer> config) {
        //config.addInstance("AuthorityListener", HibernateAuthorityEventListener.class);
    }

    public static void contributeRealmSource(Configuration<Realm> config,
            EntityManager em) {
        AclActiveDirectoryRealm realm = new AclActiveDirectoryRealm(em);
        ResourceBundle bundle = ResourceBundle.getBundle("OrionShiro");
        Enumeration<String> e = bundle.getKeys();
        realm.setSystemUsername(bundle.getString("SystemUsername"));
        realm.setSystemPassword(bundle.getString("SystemPassword"));
        realm.setSearchBase(bundle.getString("SearchBase"));
        realm.setUrl(bundle.getString("Url"));
        realm.setPrincipalSuffix(bundle.getString("PrincipalSuffix"));
        config.add(realm);
    }
}
