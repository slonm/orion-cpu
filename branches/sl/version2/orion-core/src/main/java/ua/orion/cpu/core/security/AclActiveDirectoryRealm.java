package ua.orion.cpu.core.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public AclActiveDirectoryRealm(EntityManager em) {
        this.em = em;
        this.setPermissionResolver(new AclPermissionResolver());
        this.setRolePermissionResolver(new AclRolePermissionResolver());
    }

    @Override
    protected AuthorizationInfo buildAuthorizationInfo(Set<String> roleNames, String username) {
        SimpleAuthorizationInfo saf = (SimpleAuthorizationInfo) super.buildAuthorizationInfo(roleNames, username);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Acl> query = cb.createQuery(Acl.class);
        Root<Acl> root = query.from(Acl.class);
        query.where(cb.equal(root.get("subjectType"), SubjectType.USER),
                cb.equal(root.get("subject"), username.toLowerCase()));
        for (Acl acl : em.createQuery(query).getResultList()) {
            saf.addStringPermission(acl.getPermission());
        }
        return saf;
    }

    class AclRolePermissionResolver implements RolePermissionResolver {

        @Override
        public Collection<Permission> resolvePermissionsInRole(String roleString) {
            Collection<Permission> permissions = new HashSet<Permission>();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Acl> query = cb.createQuery(Acl.class);
            Root<Acl> root = query.from(Acl.class);
            query.where(cb.equal(root.get("subjectType"), SubjectType.ROLE),
                    cb.equal(root.get("subject"), roleString.toLowerCase()));
            for (Acl acl : em.createQuery(query).getResultList()) {
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
