package org.tynamo.security.core;

import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Изменяет стратегию вычисления полномочий на множестве Realm с дизъюнкции на
 * конъюнкцию
 * @author slobodyanuk
 */
public class ConjunctionModularRealmAuthorizer extends ModularRealmAuthorizer {

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        assertRealmsConfigured();
        boolean ret = true;
        for (Realm realm : getRealms()) {
            ret = ret && realm.hasRole(principals, roleIdentifier);
        }
        return ret;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        boolean ret = true;
        for (Realm realm : getRealms()) {
            ret = ret && realm.isPermitted(principals, permission);
        }
        return ret;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        assertRealmsConfigured();
        boolean ret = true;
        for (Realm realm : getRealms()) {
            ret = ret && realm.isPermitted(principals, permission);
        }
        return ret;
    }
}
