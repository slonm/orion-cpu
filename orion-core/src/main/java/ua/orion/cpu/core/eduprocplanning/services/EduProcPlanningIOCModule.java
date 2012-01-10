package ua.orion.cpu.core.eduprocplanning.services;

import org.apache.tapestry5.ioc.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.eduprocplanning.EduProcPlanningSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class EduProcPlanningIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(EduProcPlanningSymbols.EDUPROC_PLANNING_LIB, "ua.orion.cpu.core.eduprocplanning"));
    }
}
