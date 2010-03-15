package orion.cpu.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Модуль конфигурирования IOC
 */
public class UnitsIOCModule {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(UnitsIOCModule.class);

    /**
     * Начальная инициализация базы данных
     * Создание сервиса ObjectLocatorInstance
     * @param configuration
     * @param objectLocatorInstance
     */
    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration,
            @Autobuild ObjectLocatorInstance objectLocatorInstance) {
        configuration.addInstance("UnitsInitializeDatabase", UnitsInitializeDatabase.class, "after:InitializeDatabase");
    }
}
