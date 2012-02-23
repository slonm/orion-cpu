package ua.orion.cpu.web.persons.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.persons.entities.*;
import ua.orion.cpu.core.persons.entities.Cognation;
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
     *
     * @param configuration
     * @param pageLinkCreatorFactory
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>Persons>1Person";
        configuration.add(path, mlb.buildCrudPageMenuLink(Person.class, path));

        path = "Start>Persons>FormerSurname";
        configuration.add(path, mlb.buildCrudPageMenuLink(FormerSurname.class, path));

        path = "Start>Persons>Address";
        configuration.add(path, mlb.buildCrudPageMenuLink(Address.class, path));

        path = "Start>Persons>Passport";
        configuration.add(path, mlb.buildCrudPageMenuLink(Passport.class, path));

        path = "Start>Persons>Reference>ScienceArea";
        configuration.add(path, mlb.buildCrudPageMenuLink(ScienceArea.class, path));

        path = "Start>Persons>Reference>ScientificDegree";
        configuration.add(path, mlb.buildCrudPageMenuLink(ScientificDegree.class, path));

        path = "Start>Persons>Reference>AcademicRank";
        configuration.add(path, mlb.buildCrudPageMenuLink(AcademicRank.class, path));

        path = "Start>Persons>Reference>Sex";
        configuration.add(path, mlb.buildCrudPageMenuLink(Sex.class, path));
        path = "Start>Persons>Reference>Citizenship";
        configuration.add(path, mlb.buildCrudPageMenuLink(Citizenship.class, path));
        path = "Start>Persons>Reference>FamilyStatus";
        configuration.add(path, mlb.buildCrudPageMenuLink(FamilyStatus.class, path));
        path = "Start>Persons>Reference>LabourRelations";
        configuration.add(path, mlb.buildCrudPageMenuLink(LabourRelations.class, path));
        path = "Start>Persons>Reference>TypeAgreement";
        configuration.add(path, mlb.buildCrudPageMenuLink(TypeAgreement.class, path));
        path = "Start>Persons>Reference>Retiree";
        configuration.add(path, mlb.buildCrudPageMenuLink(Retiree.class, path));
        path = "Start>Persons>Reference>Locations>1NatureResidence";
        configuration.add(path, mlb.buildCrudPageMenuLink(NatureResidence.class, path));
        path = "Start>Persons>Reference>Locations>2Country";
        configuration.add(path, mlb.buildCrudPageMenuLink(Country.class, path));
        path = "Start>Persons>Reference>Locations>3Region";
        configuration.add(path, mlb.buildCrudPageMenuLink(Region.class, path));
        path = "Start>Persons>Reference>Locations>4District";
        configuration.add(path, mlb.buildCrudPageMenuLink(District.class, path));
        path = "Start>Persons>Reference>Locations>5City";
        configuration.add(path, mlb.buildCrudPageMenuLink(City.class, path));
        path = "Start>Persons>StructureFamily";
        configuration.add(path, mlb.buildCrudPageMenuLink(StructureFamily.class, path));
        path = "Start>Persons>Reference>Cognation";
        configuration.add(path, mlb.buildCrudPageMenuLink(Cognation.class, path));

    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("persons", "ua.orion.cpu.web.persons"));
    }
}
