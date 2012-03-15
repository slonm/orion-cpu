package ua.orion.cpu.core.security.services;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import ua.orion.core.LibraryOrientedBeansFactory;
import ua.orion.core.services.ModelLibraryService;

/**
 * Realm - авторизатор на основе состояния сущности.
 * Собирает цепочку из авторизаторов, реализующих StateAuthorizer и находящихся в 
 * подпакете 'services' с именем имя_библиотеки+StateAuthorizer,
 * 
 * @author sl
 */
public class StateAuthorizerRealm extends ModularRealmAuthorizer implements Realm {

    private final StateAuthorizer authorizer;

    public StateAuthorizerRealm(ModelLibraryService resolver, ObjectLocator locator, ChainBuilder cb) {
        LibraryOrientedBeansFactory factory = new LibraryOrientedBeansFactory(resolver, locator, "services", null, "StateAuthorizer");
        authorizer = cb.build(StateAuthorizer.class, factory.create(StateAuthorizer.class));
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return !authorizer.isForbid(permission);
    }

    @Override
    public String getName() {
        return StateAuthorizerRealm.class.getSimpleName();
    }

    /**
     * Авторизация выключена
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return false;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        return true;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void assertRealmsConfigured() throws IllegalStateException {
    }
}
