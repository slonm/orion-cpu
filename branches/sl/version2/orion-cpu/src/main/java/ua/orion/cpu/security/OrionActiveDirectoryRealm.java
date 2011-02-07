/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.security;

import java.util.*;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.activedirectory.ActiveDirectoryRealm;
import org.apache.shiro.realm.ldap.*;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import ua.orion.cpu.security.entities.ActiveDirectoryPrincipal;

/**
 *
 * @author sl
 */
public class OrionActiveDirectoryRealm extends ActiveDirectoryRealm {

    @Override
    protected Collection<String> getRoleNamesForGroups(Collection<String> groupNames) {
        Set<String> roleNames = new HashSet<String>(groupNames.size());
        for (String group : groupNames) {
            String roleName = "";
            for (String cn : group.split(",")) {
                if (cn.startsWith("CN=")) {
                    if (roleName.length() > 0) {
                        roleName = "." + roleName;
                    }
                    roleName = cn.substring(3) + roleName;
                }
            }
            roleNames.add(roleName);
        }
        return roleNames;
    }

    /**
     * 
     * @param groupRolesMap 
     * @deprecated now not used
     */
    @Override
    public void setGroupRolesMap(Map<String, String> groupRolesMap) {
    }

    @Override
    protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token, LdapContextFactory ldapContextFactory) throws NamingException {
        SimpleAuthenticationInfo account = (SimpleAuthenticationInfo) super.queryForAuthenticationInfo(token, ldapContextFactory);

        // Perform context search
        LdapContext ldapContext = ldapContextFactory.getSystemLdapContext();


        if (account != null) {
            SimplePrincipalCollection principals = (SimplePrincipalCollection) account.getPrincipals();
            Object o = newActiveDirectoryPrincipal(((UsernamePasswordToken) token).getUsername(), ldapContext);
            if (o != null) {
                principals.add(o, getName());
            }
            if (!principals.isEmpty()) {
                account.setPrincipals(principals);
            }
        }
        return account;
    }

//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        Set<String> roles = new HashSet<String>();
//        roles.add(ROLE);
//        return new SimpleAuthorizationInfo(roles);
//    }
    private ActiveDirectoryPrincipal newActiveDirectoryPrincipal(String username, LdapContext ldapContext) throws NamingException {
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String userPrincipalName = username;
        if (principalSuffix != null) {
            userPrincipalName += principalSuffix;
        }

        String searchFilter = "(&(objectClass=*)(userPrincipalName={0}))";
        Object[] searchArguments = new Object[]{userPrincipalName};

        NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchArguments, searchCtls);
        ActiveDirectoryPrincipal adp = null;
        if (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();

//            if (log.isDebugEnabled()) {
//                log.debug("Retrieving group names for user [" + sr.getName() + "]");
//            }

            Attributes attrs = sr.getAttributes();
            if (attrs != null) {
                NamingEnumeration ae = attrs.getAll();
                adp = new ActiveDirectoryPrincipal();
                while (ae.hasMore()) {
                    Attribute attr = (Attribute) ae.next();
                    if (attr.getID().equalsIgnoreCase("description")) {
                        adp.setDescription((String) attr.get());
                    } else if (attr.getID().equalsIgnoreCase("CN")) {
                        adp.setFio((String) attr.get());
                    } else if (attr.getID().equalsIgnoreCase("mail")) {
                        adp.setEmail((String) attr.get());
                    } else if (attr.getID().equalsIgnoreCase("objectSID")) {
                        adp.setSid((String) attr.get());
                    } else if (attr.getID().equalsIgnoreCase("sAMAccountName")) {
                        adp.setLogin((String) attr.get());
                    }
                }
            }
        }
        return adp;
    }
}
