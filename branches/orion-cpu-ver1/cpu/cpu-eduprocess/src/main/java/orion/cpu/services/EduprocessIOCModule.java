package orion.cpu.services;

import org.apache.tapestry5.ioc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Модуль конфигурирования IOC
 */
public class EduprocessIOCModule {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(EduprocessIOCModule.class);

    /**
     * Начальная инициализация базы данных
     * Создание сервиса ObjectLocatorInstance
     * @param configuration
     */
    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration) {
        configuration.addInstance("EduprocessInitializeDatabase", EduprocessInitializeDatabase.class, "after:LicenseInitializeDatabase");
    }
}
