package ua.orion.cpu.core.security;

import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * По умолчанию части регистронезависимые. Класс позволяет получить доступ к 
 * объекту, правам и экземплярам объекта, указанным в строке разрешения.
 * @author slobodyanuk
 */
public class OrionWildcardPermission extends WildcardPermission {

    private String domain = null;
    private String action = null;
    private String instance = null;

    public OrionWildcardPermission(String wildcardString) {
        super(wildcardString, false);
    }

    @Override
    protected void setParts(String wildcardString, boolean caseSensitive) {
        super.setParts(wildcardString, caseSensitive);
        domain = getParts().get(0).iterator().next();
        if (getParts().size() >= 2) {
            action = getParts().get(1).iterator().next();
        }
        if (getParts().size() >= 3) {
            instance = getParts().get(2).iterator().next();
        }
    }

    public String getAction() {
        return action;
    }

    public String getDomain() {
        return domain;
    }

    public String getInstance() {
        return instance;
    }
}
