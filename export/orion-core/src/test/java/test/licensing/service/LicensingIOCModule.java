package test.licensing.service;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.OrionCPUSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class LicensingIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo("Licensing", "test.licensing"));
    }
}
