package ua.orion.cpu.core.students.services;

import org.apache.tapestry5.ioc.*;
import ua.orion.core.ModelLibraryInfo;
import ua.orion.cpu.core.students.StudentsSymbols;

/*
 * Модуль конфигурирования IOC
 */
public class StudentsIOCModule {

    public static void contributeModelLibraryService(Configuration<ModelLibraryInfo> conf) {
        conf.add(new ModelLibraryInfo(StudentsSymbols.STUDENTS_LIB, "ua.orion.cpu.core.students"));
    }
}
