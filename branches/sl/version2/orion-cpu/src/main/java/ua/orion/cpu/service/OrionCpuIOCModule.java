package ua.orion.cpu.service;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import ua.orion.cpu.core.OrionCPUSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class OrionCpuIOCModule {

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(OrionCPUSymbols.TEST_DATA, "false");
    }

}
