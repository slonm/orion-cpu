package ua.orion.cpu.web.licensing.pages;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

/**
 * Модуль конфигурирования IOC
 */
public class LicensingWebIOCModule {

    /**
     * Регистрация блоков для автоформирования моделей данных
     * @param configuration
     * @author sl
     */
    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
        //Edit
        configuration.add(new EditBlockContribution("TrainingDirectionOrSpeciality", "licensing/PropertyBlocks", "EditTrainingDirectionOrSpeciality"));
        configuration.add(new EditBlockContribution("KnowledgeAreaOrTrainingDirection", "licensing/PropertyBlocks", "EditKnowledgeAreaOrTrainingDirection"));
        //Display
        configuration.add(new DisplayBlockContribution("TrainingDirectionOrSpeciality", "licensing/PropertyBlocks", "DisplayTrainingDirectionOrSpeciality"));
        configuration.add(new DisplayBlockContribution("KnowledgeAreaOrTrainingDirection", "licensing/PropertyBlocks", "DisplayKnowledgeAreaOrTrainingDirection"));
    }

    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>Licensing>License";
        configuration.add(path, mlb.buildCrudPageMenuLink(License.class, path));

        path = "Start>Licensing>LicenseRecord";
        configuration.add(path, mlb.buildCrudPageMenuLink(LicenseRecord.class, path));

        path = "Start>Licensing>License>KnowledgeAreaOrTrainingDirection";
        configuration.add(path, mlb.buildCrudPageMenuLink(KnowledgeAreaOrTrainingDirection.class, path));

        path = "Start>Licensing>License>TrainingDirectionOrSpeciality";
        configuration.add(path, mlb.buildCrudPageMenuLink(TrainingDirectionOrSpeciality.class, path));

        path = "Start>Licensing>License>LicenseRecordGroup";
        configuration.add(path, mlb.buildCrudPageMenuLink(LicenseRecordGroup.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("licensing", "ua.orion.cpu.web.licensing"));
    }
}
