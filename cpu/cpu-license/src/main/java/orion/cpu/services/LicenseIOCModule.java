package orion.cpu.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Модуль конфигурирования IOC
 */
public class LicenseIOCModule {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LicenseIOCModule.class);

    /**
     * Начальная инициализация базы данных
     * Создание сервиса ObjectLocatorInstance
     * @param configuration
     * @param objectLocatorInstance
     */
    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration) {
        configuration.addInstance("LicenseInitializeDatabase", LicenseInitializeDatabase.class, "after:UnitsInitializeDatabase");
    }
}
