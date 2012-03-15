package ua.orion.cpu.core.persons.services;

import org.apache.tapestry5.ioc.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.persons.PersonsSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class PersonsIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(PersonsSymbols.PERSONS_LIB, "ua.orion.cpu.core.persons"));
    }
}
