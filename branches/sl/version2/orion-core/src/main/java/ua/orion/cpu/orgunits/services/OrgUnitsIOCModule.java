package ua.orion.cpu.orgunits.services;

import ua.orion.cpu.licensing.services.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.OrionCPUSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class OrgUnitsIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("OrgUnits", "ua.orion.cpu.orgunits"));
    }
}
