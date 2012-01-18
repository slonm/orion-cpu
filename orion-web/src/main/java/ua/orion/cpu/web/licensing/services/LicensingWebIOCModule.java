package ua.orion.cpu.web.licensing.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.services.*;
import ua.orion.core.services.EntityService;
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
        //Display EduFormLicenseQuantity
        configuration.add(new DisplayBlockContribution("EduFormLicenseQuantity", "licensing/PropertyBlocks", "DisplayEduFormLicenseQuantity"));
        //Edit EduFormLicenseQuantity
        configuration.add(new EditBlockContribution("EduFormLicenseQuantity", "licensing/PropertyBlocks", "EditEduFormLicenseQuantity"));
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

        path = "Start>Licensing>Reference";

        path = "Start>Licensing>Reference>KnowledgeArea";
        configuration.add(path, mlb.buildCrudPageMenuLink(KnowledgeArea.class, path));

        path = "Start>Licensing>Reference>TrainingDirectionOrSpeciality";
        configuration.add(path, mlb.buildCrudPageMenuLink(TrainingDirection.class, path));

        path = "Start>Licensing>Reference>Speciality";
        configuration.add(path, mlb.buildCrudPageMenuLink(Speciality.class, path));

        path = "Start>Licensing>Reference>LicenseRecordGroup";
        configuration.add(path, mlb.buildCrudPageMenuLink(LicenseRecordGroup.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("licensing", "ua.orion.cpu.web.licensing"));
    }

    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration,
            final EntityService entityService) {
        configuration.add(new Coercion<IMenuLink, Class<License>>() {

            @Override
            public Class<License> coerce(IMenuLink input) {
                try {
                    return ("licensing/Licenses".equalsIgnoreCase(input.getPage())
                            || "licensing/License".equalsIgnoreCase(input.getPage()))
                            ? License.class
                            : null;
                } catch (Exception ex) {
                }
                return null;
            }
        });
    }
}
