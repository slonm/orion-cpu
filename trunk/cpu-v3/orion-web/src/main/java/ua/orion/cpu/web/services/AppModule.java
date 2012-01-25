package ua.orion.cpu.web.services;

import java.util.Enumeration;
import java.util.ResourceBundle;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.SubModule;

/**
 * Модуль конфигурирования IOC
 */
@SubModule({
    ua.orion.web.services.OrionWebIOCModule.class,
    ua.orion.cpu.core.security.services.OrionSecurityIOCModule.class,
    ua.orion.cpu.core.services.OrionCpuIOCModule.class,
    ua.orion.cpu.core.licensing.services.LicensingIOCModule.class,
    ua.orion.cpu.core.eduprocplanning.services.EduProcPlanningIOCModule.class,
    ua.orion.cpu.core.orgunits.services.OrgUnitsIOCModule.class,
    ua.orion.cpu.core.persons.services.PersonsIOCModule.class,
    ua.orion.cpu.core.students.services.StudentsIOCModule.class,
    ua.orion.cpu.core.employees.services.EmployeesIOCModule.class,
    ua.orion.web.security.services.OrionSecurityWebIOCModule.class,
    ua.orion.cpu.web.licensing.services.LicensingWebIOCModule.class,
    ua.orion.cpu.web.eduprocplanning.services.EduProcPlanningWebIOCModule.class,
    ua.orion.cpu.web.orgunits.services.OrgUnitsWebIOCModule.class,
    ua.orion.cpu.web.persons.services.PersonsWebIOCModule.class,
    ua.orion.cpu.web.students.services.StudentsWebIOCModule.class,
    ua.orion.cpu.web.employees.services.EmployeesWebIOCModule.class
})
public class AppModule {

    public static void bind(ServiceBinder binder) {
    }

    /**
     * Регистрация и конфигурирование опций.
     * @param configuration
     */
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {
        ResourceBundle bundle = ResourceBundle.getBundle("Cpu");
        Enumeration<String> e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            configuration.add(key, bundle.getString(key));
        }
    }
}
