/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.core.security;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import javax.persistence.EntityManager;
import ua.orion.cpu.core.security.services.ThreadRole;

/**
 *
 * @author slobodyanuk
 */
public class ThreadLocalRoleRealm extends AclActiveDirectoryRealm {

    private final ThreadRole threadRole;

    public ThreadLocalRoleRealm(EntityManager em, ThreadRole threadRole) {
        super(em);
        assert threadRole != null;
        this.threadRole = threadRole;
    }

    @Override
    protected Set<String> getRoleNamesForUser(String username, LdapContext ldapContext) throws NamingException {
        Set<String> roleNames = super.getRoleNamesForUser(username, ldapContext);
        Iterator<String> it = roleNames.iterator();
        String thRole = threadRole.getRole();
        if (thRole == null) {
            return Collections.EMPTY_SET;
        }
        while (it.hasNext()) {
            String role = it.next();
            if (!role.equalsIgnoreCase(thRole)) {
                it.remove();
            } else {
                threadRole.setRole(role);
            }
        }
        if (roleNames.isEmpty()) {
            threadRole.setRole(null);
        }
        return roleNames;
    }
}
