package ua.orion.cpu.core.security.services;

/**
 * 
 * @author slobodyanuk
 */
public interface StateAuthorizer {
    /**
     * Возвращает <tt>true</tt> если состояние объекта не позволяет делать с ним 
     * действие, указанное в строке permission, иначе false.
     *
     * @param permission the String representation of a Permission that is being checked.
     * @see #isPermitted(PrincipalCollection principals,Permission permission)
     */
    boolean isForbid(String permission);
}
