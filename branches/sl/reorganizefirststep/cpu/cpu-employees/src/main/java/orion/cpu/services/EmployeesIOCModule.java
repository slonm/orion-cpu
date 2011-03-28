package orion.cpu.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Модуль конфигурирования IOC
 */
public class EmployeesIOCModule {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(EmployeesIOCModule.class);

    /**
     * Начальная инициализация базы данных
     * Создание сервиса ObjectLocatorInstance
     * @param configuration
     * @param objectLocatorInstance
     */
    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration) {
        configuration.addInstance("EmployeesInitializeDatabase", EmployeesInitializeDatabase.class, "after:UnitsInitializeDatabase,PersonalInitializeDatabase");
    }
}
