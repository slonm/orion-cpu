package ua.orion.cpu.web.students.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.students.entities.*;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

/**
 * Модуль конфигурирования IOC
 */


public class StudentsWebIOCModule {

    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>Persons>Students>3Student";
        configuration.add(path, mlb.buildCrudPageMenuLink(Student.class, path));
        path = "Start>Persons>Students>2AcademicGroup";
        configuration.add(path, mlb.buildCrudPageMenuLink(AcademicGroup.class, path));
        path = "Start>Persons>Students>1AcademicStream";
        configuration.add(path, mlb.buildCrudPageMenuLink(AcademicStream.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("students", "ua.orion.cpu.web.students"));
    }
}
