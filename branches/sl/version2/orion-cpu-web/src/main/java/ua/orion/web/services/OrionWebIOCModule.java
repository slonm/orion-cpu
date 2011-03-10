package ua.orion.web.services;

import org.apache.shiro.realm.Realm;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.tynamo.shiro.extension.realm.text.ExtendedPropertiesRealm;

/**
 *
 * @author slobodyanuk
 */
public class OrionWebIOCModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(TapestryComponentDataSource.class, TapestryComponentDataSourceImpl.class);
    }

    public static void contributeRealmSource(Configuration<Realm> configuration) {
        ExtendedPropertiesRealm realm = new ExtendedPropertiesRealm("classpath:shiro-users.properties");
        configuration.add(realm);
    }
}
