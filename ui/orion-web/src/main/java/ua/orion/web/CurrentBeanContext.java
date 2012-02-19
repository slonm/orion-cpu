package ua.orion.web;

/**
 * Контекст хранит текущий бин
 * @author slobodyanuk
 */
public interface CurrentBeanContext {
    Object getCurrentBean();
    Class<?> getBeanType();
}
