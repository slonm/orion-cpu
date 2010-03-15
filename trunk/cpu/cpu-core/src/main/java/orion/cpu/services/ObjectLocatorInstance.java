package orion.cpu.services;

import org.apache.tapestry5.ioc.ObjectLocator;

/**
 * Сервис хранит в статической переменной ссылку на ObjectLocator.
 * @author sl
 */
public class ObjectLocatorInstance {

    private static ObjectLocator INSTANCE = null;

    public ObjectLocatorInstance(ObjectLocator objectLocator) {
        INSTANCE = objectLocator;
    }

    public static ObjectLocator getINSTANCE() {
        return INSTANCE;
    }
}
