package ua.orion.cpu.core.security.services;

/**
 * Интерфейс авторизатора операции на основе состояния объекта.
 * @author slobodyanuk
 */
public interface StateAuthorizer {

    /**
     * Возвращает <tt>true</tt> если состояние объекта не позволяет делать с ним 
     * действие, указанное в строке permission, иначе false.
     *
     * @param permission the String representation of a Permission that is being checked.
     */
    boolean isForbid(String permission);
}
