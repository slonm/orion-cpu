package ua.orion.cpu.core.security;

import java.util.*;
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

    public AclActiveDirectoryRealm(EntityManager em) {
        this.em = em;
        this.setPermissionResolver(new AclPermissionResolver());
        this.setRolePermissionResolver(new AclRolePermissionResolver());
    }

    @Override
    protected AuthorizationInfo buildAuthorizationInfo(Set<String> roleNames, String username) {
        SimpleAuthorizationInfo saf = (SimpleAuthorizationInfo) super.buildAuthorizationInfo(roleNames, username);
        for (Permission permission : resolvePermissionsForSubject(username, SubjectType.USER)) {
            saf.addObjectPermission(permission);
        }
        return saf;
    }

    protected Collection<Permission> resolvePermissionsForSubject(String subject, SubjectType type) {
        Collection<Permission> permissions = new HashSet<Permission>();
        String qStr = "FROM Acl WHERE lower(subject) = lower(:subject) and subjectType=:subjectType";
        TypedQuery<Acl> query = em.createQuery(qStr, Acl.class);
        query.setParameter("subject", subject);
        query.setParameter("subjectType", type);
        for (Acl acl : query.getResultList()) {
            permissions.add(new MyWildcardPermission(acl.getPermission()));
        }
        Set<MyWildcardPermission> list = new HashSet();
        list.addAll((Set<MyWildcardPermission>) (Set) permissions);
        for (MyWildcardPermission p : list) {
            if (p.getObject().toLowerCase().startsWith("permg_")) {
                permissions.addAll(resolvePermissionsForSubject(
                        p.getObject().substring("permg_".length()),
                        SubjectType.PERMISSION_GROUP));
            }
        }
        return permissions;
    }

    class AclRolePermissionResolver implements RolePermissionResolver {

        @Override
        public Collection<Permission> resolvePermissionsInRole(String roleString) {
            return resolvePermissionsForSubject(roleString, SubjectType.ROLE);
        }
    }

    class AclPermissionResolver implements PermissionResolver {

        @Override
        public Permission resolvePermission(String permissionString) {
            return new MyWildcardPermission(permissionString);
        }
    }

    static class MyWildcardPermission extends WildcardPermission {

        public MyWildcardPermission(String wildcardString) {
            super(wildcardString);
        }

        String getObject() {
            Set<String> part0 = getParts().get(0);
            for (String s : part0) {
                return s;
            }
            return null;
        }
    }
}
