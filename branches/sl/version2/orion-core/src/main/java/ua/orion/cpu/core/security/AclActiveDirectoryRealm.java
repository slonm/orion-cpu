package ua.orion.cpu.core.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import ua.orion.cpu.core.security.entities.*;

/**
 * Realm извлекает авторизационную информацию из таблицы Acl
 * @author sl
 */
public class AclActiveDirectoryRealm extends OrionActiveDirectoryRealm {

    private EntityManager em;
    private final String QL = "FROM Acl WHERE lower(subject) = :subject and subjectType=:subjectType";

    public AclActiveDirectoryRealm(EntityManager em) {
        this.em = em;
        this.setPermissionResolver(new AclPermissionResolver());
        this.setRolePermissionResolver(new AclRolePermissionResolver());
    }

    @Override
    protected AuthorizationInfo buildAuthorizationInfo(Set<String> roleNames, String username) {
        SimpleAuthorizationInfo saf = (SimpleAuthorizationInfo) super.buildAuthorizationInfo(roleNames, username);
        TypedQuery<Acl> q = em.createQuery(QL, Acl.class);
        q.setParameter("subject", username.toLowerCase());
        q.setParameter("subjectType", SubjectType.USER);
        for (Acl acl : q.getResultList()) {
            saf.addStringPermission(acl.getPermission());
        }
        return saf;
    }

    class AclRolePermissionResolver implements RolePermissionResolver {

        @Override
        public Collection<Permission> resolvePermissionsInRole(String roleString) {
            Collection<Permission> permissions = new HashSet<Permission>();
            TypedQuery<Acl> q = em.createQuery(QL, Acl.class);
            q.setParameter("subject", roleString.toLowerCase());
            q.setParameter("subjectType", SubjectType.ROLE);
            for (Acl acl : q.getResultList()) {
                permissions.add(new WildcardPermission(acl.getPermission()));
            }
            return permissions;
        }
    }

    class AclPermissionResolver implements PermissionResolver {

        @Override
        public Permission resolvePermission(String permissionString) {
            return new WildcardPermission(permissionString);
        }
    }
}
