package orion.cpu.utils;

/**
 * Возможные операции с классом/объектом
 * @author sl
 */
public @interface PossibleOperations {

    /**
     * Возможные операции с классом/объектом
     */
    String[] value();

    /**
     * Наследовать возможные операции с классом/объектом
     */
    boolean inherit() default true;
}
