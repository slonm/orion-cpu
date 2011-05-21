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
import ua.orion.cpu.core.security.services.ThreadRole;

/**
 * Realm извлекает авторизационную информацию из таблицы Acl и добавляет поддержку динамических ролей
 * @author sl
 */
public class AclActiveDirectoryRealm extends OrionActiveDirectoryRealm {

    private final EntityManager em;
    private final ThreadRole threadRole;

    public AclActiveDirectoryRealm(EntityManager em, ThreadRole threadRole) {
        assert threadRole != null;
        assert em != null;
        this.em = em;
        this.threadRole = threadRole;
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
            if (!roleString.equalsIgnoreCase(threadRole.getRole())) {
                return Collections.EMPTY_SET;
            }
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
