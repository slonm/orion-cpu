package ua.orion.cpu.core.licensing.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.licensing.LicensingSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class LicensingIOCModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(LicensingService.class, LicensingServiceImpl.class);
    }
    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(LicensingSymbols.LICENSING_LIB, "ua.orion.cpu.core.licensing"));
    }
}
