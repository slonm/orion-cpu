/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.security;

import java.util.*;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.ldap.*;
import org.apache.shiro.subject.SimplePrincipalCollection;
import ua.orion.cpu.core.security.entities.ActiveDirectoryPrincipal;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;

/**
 * Realm получает из Active Directory информацию о пользователе и создает principal типа
 * ActiveDirectoryPrincipal. Кроме этого отображает имена групп AD на имена ролей
 * @author sl
 */
public class OrionActiveDirectoryRealm extends AuthorizingRealm {

    /*--------------------------------------------
    |             C O N S T A N T S             |
    ============================================*/
    private static final Logger log = LoggerFactory.getLogger(AbstractLdapRealm.class);

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    protected String principalSuffix = null;
    protected String searchBase = null;
    protected String url = null;
    protected String systemUsername = null;
    protected String systemPassword = null;
    private LdapContextFactory ldapContextFactory = null;

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    /*--------------------------------------------
    |               M E T H O D S               |
    ============================================*/
    /**
     * Used when initializing the default {@link LdapContextFactory}.  This property is ignored if a custom
     * <tt>LdapContextFactory</tt> is specified.
     *
     * @param principalSuffix the suffix.
     * @see DefaultLdapContextFactory#setPrincipalSuffix(String)
     */
    public void setPrincipalSuffix(String principalSuffix) {
        this.principalSuffix = principalSuffix;
    }

    /**
     * Used when initializing the default {@link LdapContextFactory}.  This property is ignored if a custom
     * <tt>LdapContextFactory</tt> is specified.
     *
     * @param searchBase the search base.
     * @see DefaultLdapContextFactory#setSearchBase(String)
     */
    public void setSearchBase(String searchBase) {
        this.searchBase = searchBase;
    }

    /**
     * Used when initializing the default {@link LdapContextFactory}.  This property is ignored if a custom
     * <tt>LdapContextFactory</tt> is specified.
     *
     * @param url the LDAP url.
     * @see DefaultLdapContextFactory#setUrl(String)
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Used when initializing the default {@link LdapContextFactory}.  This property is ignored if a custom
     * <tt>LdapContextFactory</tt> is specified.
     *
     * @param systemUsername the username to use when logging into the LDAP server for authorization.
     * @see DefaultLdapContextFactory#setSystemUsername(String)
     */
    public void setSystemUsername(String systemUsername) {
        this.systemUsername = systemUsername;
    }

    /**
     * Used when initializing the default {@link LdapContextFactory}.  This property is ignored if a custom
     * <tt>LdapContextFactory</tt> is specified.
     *
     * @param systemPassword the password to use when logging into the LDAP server for authorization.
     * @see DefaultLdapContextFactory#setSystemPassword(String)
     */
    public void setSystemPassword(String systemPassword) {
        this.systemPassword = systemPassword;
    }

    /**
     * Configures the {@link LdapContextFactory} implementation that is used to create LDAP connections for
     * authentication and authorization.  If this is set, the {@link LdapContextFactory} provided will be used.
     * Otherwise, a {@link DefaultLdapContextFactory} instance will be created based on the properties specified
     * in this realm.
     *
     * @param ldapContextFactory the factory to use - if not specified, a default factory will be created automatically.
     */
    public void setLdapContextFactory(LdapContextFactory ldapContextFactory) {
        this.ldapContextFactory = ldapContextFactory;
    }

    /*--------------------------------------------
    |               M E T H O D S                |
    ============================================*/
    protected void onInit() {
        ensureContextFactory();
    }

    private LdapContextFactory ensureContextFactory() {
        if (this.ldapContextFactory == null) {

            if (log.isDebugEnabled()) {
                log.debug("No LdapContextFactory specified - creating a default instance.");
            }
            DefaultLdapContextFactory defaultFactory = new DefaultLdapContextFactory();
            defaultFactory.setPrincipalSuffix(this.principalSuffix);
            defaultFactory.setUrl(this.url);
            defaultFactory.setSystemUsername(this.systemUsername);
            defaultFactory.setSystemPassword(this.systemPassword);
            Map<String, String> additionalEnvironment = new HashMap<String, String>();
            additionalEnvironment.put("java.naming.ldap.attributes.binary", "objectSid");
            defaultFactory.setAdditionalEnvironment(additionalEnvironment);
            this.ldapContextFactory = defaultFactory;
        }
        return this.ldapContextFactory;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            info = queryForAuthenticationInfo(token, ensureContextFactory());
        } catch (javax.naming.AuthenticationException e) {
            throw new AuthenticationException("LDAP authentication failed.", e);
        } catch (NamingException e) {
            String msg = "LDAP naming error while attempting to authenticate user.";
            throw new AuthenticationException(msg, e);
        }

        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AuthorizationInfo info;
        try {
            info = queryForAuthorizationInfo(principals, ensureContextFactory());
        } catch (NamingException e) {
            String msg = "LDAP naming error while attempting to retrieve authorization for user [" + principals + "].";
            throw new AuthorizationException(msg, e);
        }

        return info;
    }

    /**
     * This method is called by the default implementation to translate Active Directory group names
     * to role names.  This implementation uses the {@link #groupRolesMap} to map group names to role names.
     *
     * @param groupNames the group names that apply to the current user.
     * @return a collection of roles that are implied by the given role names.
     */
    protected Collection<String> getRoleNamesForGroups(Collection<String> groupNames) {
        Set<String> roleNames = new HashSet<String>(groupNames.size());
        for (String group : groupNames) {
            roleNames.add(distinguishedGroupNameToRoleName(group));
        }
        return roleNames;
    }

    protected static String distinguishedGroupNameToRoleName(String dName) {
        String roleName = "";
        for (String cn : dName.split(",")) {
            if (cn.startsWith("CN=")) {
                if (roleName.length() > 0) {
                    roleName = "." + roleName;
                }
                roleName = cn.substring(3) + roleName;
            }
        }
        return roleName.toLowerCase();
    }

    /**
     * Builds an {@link AuthenticationInfo} object by querying the active directory LDAP context for the
     * specified username.  This method binds to the LDAP server using the provided username and password -
     * which if successful, indicates that the password is correct.
     * <p/>
     * This method can be overridden by subclasses to query the LDAP server in a more complex way.
     *
     * @param token              the authentication token provided by the user.
     * @param ldapContextFactory the factory used to build connections to the LDAP server.
     * @return an {@link AuthenticationInfo} instance containing information retrieved from LDAP.
     * @throws NamingException if any LDAP errors occur during the search.
     */
    protected AuthenticationInfo queryForAuthenticationInfo(AuthenticationToken token, LdapContextFactory ldapContextFactory) throws NamingException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        // Binds using the username and password provided by the user.
        LdapContext ctx = null;
        try {
            ctx = ldapContextFactory.getLdapContext(upToken.getUsername(), String.valueOf(upToken.getPassword()));
        } finally {
            LdapUtils.closeContext(ctx);
        }

        SimpleAuthenticationInfo account = (SimpleAuthenticationInfo) buildAuthenticationInfo(upToken.getUsername(), upToken.getPassword());

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

    protected AuthenticationInfo buildAuthenticationInfo(String username, char[] password) {
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    /**
     * Builds an {@link org.apache.shiro.authz.AuthorizationInfo} object by querying the active directory LDAP context for the
     * groups that a user is a member of.  The groups are then translated to role names by using the
     * configured {@link #groupRolesMap}.
     * <p/>
     * This implementation expects the <tt>principal</tt> argument to be a String username.
     * <p/>
     * Subclasses can override this method to determine authorization data (roles, permissions, etc) in a more
     * complex way.  Note that this default implementation does not support permissions, only roles.
     *
     * @param principals         the principal of the Subject whose account is being retrieved.
     * @param ldapContextFactory the factory used to create LDAP connections.
     * @return the AuthorizationInfo for the given Subject principal.
     * @throws NamingException if an error occurs when searching the LDAP server.
     */
    protected AuthorizationInfo queryForAuthorizationInfo(PrincipalCollection principals, LdapContextFactory ldapContextFactory) throws NamingException {

        String username = (String) getAvailablePrincipal(principals);

        // Perform context search
        LdapContext ldapContext = ldapContextFactory.getSystemLdapContext();

        Set<String> roleNames;

        try {
            roleNames = getRoleNamesForUser(username, ldapContext);
        } finally {
            LdapUtils.closeContext(ldapContext);
        }

        return buildAuthorizationInfo(roleNames, username);
    }

    protected AuthorizationInfo buildAuthorizationInfo(Set<String> roleNames, String username) {
        return new SimpleAuthorizationInfo(roleNames);
    }

    protected Set<String> getRoleNamesForUser(String username, LdapContext ldapContext) throws NamingException {
        Set<String> roleNames;
        roleNames = new LinkedHashSet<String>();

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String userPrincipalName = username;
        if (principalSuffix != null) {
            userPrincipalName += principalSuffix;
        }

        String searchFilter = "(&(objectClass=*)(userPrincipalName={0}))";
        Object[] searchArguments = new Object[]{userPrincipalName};

        NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchArguments, searchCtls);

        while (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();

            if (log.isDebugEnabled()) {
                log.debug("Retrieving group names for user [" + sr.getName() + "]");
            }

            Attributes attrs = sr.getAttributes();

            if (attrs != null) {
                NamingEnumeration ae = attrs.getAll();
                String primaryGroupId = null;
                String userSid = null;
                while (ae.hasMore()) {
                    Attribute attr = (Attribute) ae.next();

                    if (attr.getID().equalsIgnoreCase("primaryGroupID")) {
                        primaryGroupId = String.valueOf(attr.get());
                    } else if (attr.getID().equalsIgnoreCase("objectSid")) {
                        userSid = getSIDAsString((byte[]) attr.get());
                    } else if (attr.getID().equalsIgnoreCase("memberOf")) {

                        Collection<String> groupNames = LdapUtils.getAllAttributeValues(attr);

                        if (log.isDebugEnabled()) {
                            log.debug("Groups found for user [" + username + "]: " + groupNames);
                        }

                        Collection<String> rolesForGroups = getRoleNamesForGroups(groupNames);
                        roleNames.addAll(rolesForGroups);
                    }
                }
                String primaryGroup = bindPrimaryGroup(primaryGroupSID(userSid, primaryGroupId), ldapContext);
                if (primaryGroup != null) {
                    roleNames.add(primaryGroup);
                }
            }
        }
        return roleNames;
    }

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

            if (log.isDebugEnabled()) {
                log.debug("Retrieving attributes names for user [" + sr.getName() + "]");
            }

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
                        adp.setSid(getSIDAsString((byte[]) attr.get()));
                    } else if (attr.getID().equalsIgnoreCase("sAMAccountName")) {
                        adp.setLogin((String) attr.get());
                    }
                }
            }
        }
        return adp;
    }

// This method is used to bind the primary group and bind
    private String bindPrimaryGroup(String sidBytes, LdapContext ldapContext) throws NamingException {
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = "(&(objectClass=*)(objectSid={0}))";
        Object[] searchArguments = new Object[]{sidBytes};

        NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchArguments, searchCtls);

        if (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();

            if (log.isDebugEnabled()) {
                log.debug("Retrieving group name for sid [" + sidBytes + "]");
            }

            Attributes attrs = sr.getAttributes();

            if (attrs != null) {
                NamingEnumeration ae = attrs.getAll();
                while (ae.hasMore()) {
                    Attribute attr = (Attribute) ae.next();

                    if (attr.getID().equals("distinguishedName")) {
                        return distinguishedGroupNameToRoleName((String) attr.get());
                    }
                }
            }
        }
        return null;
    }

    private static String primaryGroupSID(String userSid, String primaryGroupID) {
        return userSid.substring(0, userSid.lastIndexOf("-")+1)+primaryGroupID;
    }

    public static String getSIDAsString(byte[] SID) {
        // Add the 'S' prefix
        StringBuilder strSID = new StringBuilder("S-");

        // bytes[0] : in the array is the version (must be 1 but might 
        // change in the future)
        strSID.append(SID[0]).append('-');

        // bytes[2..7] : the Authority
        StringBuilder tmpBuff = new StringBuilder();
        for (int t = 2; t <= 7; t++) {
            String hexString = Integer.toHexString(SID[t] & 0xFF);
            tmpBuff.append(hexString);
        }
        strSID.append(Long.parseLong(tmpBuff.toString(), 16));

        // bytes[1] : the sub authorities count
        int count = SID[1];

        // bytes[8..end] : the sub authorities (these are Integers - notice
        // the endian)
        for (int i = 0; i < count; i++) {
            int currSubAuthOffset = i * 4;
            tmpBuff.setLength(0);
            tmpBuff.append(String.format("%02X%02X%02X%02X",
                    (SID[11 + currSubAuthOffset] & 0xFF),
                    (SID[10 + currSubAuthOffset] & 0xFF),
                    (SID[9 + currSubAuthOffset] & 0xFF),
                    (SID[8 + currSubAuthOffset] & 0xFF)));

            strSID.append('-').append(Long.parseLong(tmpBuff.toString(), 16));
        }

        // That's it - we have the SID
        return strSID.toString();
    }
    
    @Override
    protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        return info != null && info.getRoles() != null && info.getRoles().contains(roleIdentifier.toLowerCase());
    }
}
