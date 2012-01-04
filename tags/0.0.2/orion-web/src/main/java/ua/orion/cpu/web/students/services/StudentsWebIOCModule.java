package ua.orion.cpu.web.students.services;

import ua.orion.cpu.core.persons.entities.StructureFamily;
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
     *
     * @param configuration
     * @param pageLinkCreatorFactory
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;
        path = "Start>Persons>Students>6SocialPrivelege";
        configuration.add(path, mlb.buildCrudPageMenuLink(SocialPrivelege.class, path));
        path = "Start>Persons>Students>5ContractStudent";
        configuration.add(path, mlb.buildCrudPageMenuLink(ContractStudent.class, path));
        path = "Start>Persons>Students>4EducationStudent";
        configuration.add(path, mlb.buildCrudPageMenuLink(EducationStudent.class, path));
        path = "Start>Persons>Students>3Student";
        configuration.add(path, mlb.buildCrudPageMenuLink(Student.class, path));
        path = "Start>Persons>Students>2AcademicGroup";
        configuration.add(path, mlb.buildCrudPageMenuLink(AcademicGroup.class, path));
        path = "Start>Persons>Students>1AcademicStream";
        configuration.add(path, mlb.buildCrudPageMenuLink(AcademicStream.class, path));
        path = "Start>Persons>Students>Reference>Education";
        configuration.add(path, mlb.buildCrudPageMenuLink(Education.class, path));
        path = "Start>Persons>Students>Reference>KindPayer";
        configuration.add(path, mlb.buildCrudPageMenuLink(KindPayer.class, path));
        path = "Start>Persons>Students>Reference>KindPrevelege";
        configuration.add(path, mlb.buildCrudPageMenuLink(KindPrevelege.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("students", "ua.orion.cpu.web.students"));
    }
}
