package ua.orion.cpu.web.persons.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.persons.entities.AcademicRank;
import ua.orion.cpu.core.persons.entities.Person;
import ua.orion.cpu.core.persons.entities.ScienceArea;
import ua.orion.cpu.core.persons.entities.ScientificDegree;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

/**
 * Модуль конфигурирования IOC
 */
public class PersonsWebIOCModule {

//    /**
//     * Регистрация блоков для автоформирования моделей данных
//     * @param configuration
//     * @author sl
//     */
//    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
//    }
    
    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>Persons>Person";
        configuration.add(path, mlb.buildCrudPageMenuLink(Person.class, path));
        
        path = "Start>Persons>Reference";
        
        path = "Start>Persons>Reference>ScienceArea";
        configuration.add(path, mlb.buildCrudPageMenuLink(ScienceArea.class, path));
        
        path = "Start>Persons>Reference>ScientificDegree";
        configuration.add(path, mlb.buildCrudPageMenuLink(ScientificDegree.class, path));
        
        path = "Start>Persons>Reference>AcademicRank";
        configuration.add(path, mlb.buildCrudPageMenuLink(AcademicRank.class, path));

        

    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("persons", "ua.orion.cpu.web.persons"));
    }
}
