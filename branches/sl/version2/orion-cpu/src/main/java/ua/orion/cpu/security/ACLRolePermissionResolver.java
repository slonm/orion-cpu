package ua.orion.cpu.security;

import java.util.*;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 *
 * @author sl
 */
public class ACLRolePermissionResolver implements RolePermissionResolver{

    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
                Collection<Permission> permissions = new HashSet<Permission>();
                permissions.add( new WildcardPermission( roleString + ":perm1" ) );
                permissions.add( new WildcardPermission( roleString + ":perm2" ) );
                permissions.add( new WildcardPermission( "other:*:foo" ) );
                return permissions;
    }

}
