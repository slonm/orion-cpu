package ua.orion.cpu.core.security;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
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

    protected Collection<OrionWildcardPermission> resolvePermissionsForSubject(String subject, SubjectType type) {
        Collection<OrionWildcardPermission> permissions = new HashSet<>();
        String qStr = "FROM Acl WHERE lower(subject) = lower(:subject) and subjectType=:subjectType";
        TypedQuery<Acl> query = em.createQuery(qStr, Acl.class);
        query.setParameter("subject", subject);
        query.setParameter("subjectType", type);
        for (Acl acl : query.getResultList()) {
            permissions.add(new OrionWildcardPermission(acl.getPermission()));
        }
        Set<OrionWildcardPermission> list = new HashSet();
        list.addAll(permissions);
        for (OrionWildcardPermission p : list) {
            if (p.getDomain().startsWith("permg_")) {
                permissions.addAll(resolvePermissionsForSubject(
                        p.getDomain().substring("permg_".length()),
                        SubjectType.PERMISSION_GROUP));
            }
        }
        return permissions;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final String user = principals.oneByType(String.class);
        if (user.startsWith("role_")) {
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            info.setObjectPermissions(new HashSet(resolvePermissionsForSubject(user.substring(5), SubjectType.ROLE)));
            return info;
        } else {
            return super.doGetAuthorizationInfo(principals);
        }
    }

    class AclRolePermissionResolver implements RolePermissionResolver {

        @Override
        public Collection<Permission> resolvePermissionsInRole(String roleString) {
            if (!roleString.equalsIgnoreCase(threadRole.getRole())) {
                return Collections.EMPTY_SET;
            }
            return getAuthorizationInfo(new SimplePrincipalCollection("role_"+roleString, "role")).getObjectPermissions();
        }
    }

    class AclPermissionResolver implements PermissionResolver {

        @Override
        public Permission resolvePermission(String permissionString) {
            return new OrionWildcardPermission(permissionString);
        }
    }
}
