package orion.cpu.views.tapestry.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.services.*;
import orion.cpu.entities.ref.*;
import orion.cpu.entities.uch.*;
import orion.cpu.views.tapestry.pages.uch.ListLicenseRecordView;
import orion.tapestry.menu.lib.IMenuLink;

/**
 * Модуль конфигурирования IOC
 */
public class LicenseTapestryIOCModule {

    /**
     * Регистрация блоков для автоформирования моделей данных
     * @param configuration
     * @author sl
     */
    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
        //Edit
        configuration.add(new BeanBlockContribution("TrainingDirectionOrSpeciality", "LicensePropertyBlocks", "EditTrainingDirectionOrSpeciality", true));
        configuration.add(new BeanBlockContribution("KnowledgeAreaOrTrainingDirection", "LicensePropertyBlocks", "EditKnowledgeAreaOrTrainingDirection", true));
        //Display
        configuration.add(new BeanBlockContribution("TrainingDirectionOrSpeciality", "LicensePropertyBlocks", "DisplayTrainingDirectionOrSpeciality", false));
        configuration.add(new BeanBlockContribution("KnowledgeAreaOrTrainingDirection", "LicensePropertyBlocks", "DisplayKnowledgeAreaOrTrainingDirection", false));
    }

    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>License>License";
        configuration.add(path, mlb.buildListPageMenuLink(License.class, path));

        path = "Start>License>LicenseRecord";
        configuration.add(path, mlb.buildListPageMenuLink(LicenseRecord.class, path));

        path = ListLicenseRecordView.MENU_PATH;
        configuration.add(path, mlb.buildListPageMenuLink(LicenseRecordView.class, path));

        path = "Start>License>LicenseRecordView>KnowledgeAreaOrTrainingDirection";
        configuration.add(path, mlb.buildListPageMenuLink(KnowledgeAreaOrTrainingDirection.class, path));

        path = "Start>License>LicenseRecordView>TrainingDirectionOrSpeciality";
        configuration.add(path, mlb.buildListPageMenuLink(TrainingDirectionOrSpeciality.class, path));

        path = "Start>License>LicenseRecordView>LicenseRecordGroup";
        configuration.add(path, mlb.buildListPageMenuLink(LicenseRecordGroup.class, path));
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("License", "classpath:License.properties");
        configuration.add("LicenseTapestry", "classpath:LicenseTapestry.properties");
    }

    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration) {
        configuration.add(new Coercion<IMenuLink, Class<LicenseRecordView>>() {

            @Override
            public Class<LicenseRecordView> coerce(IMenuLink input) {
                if (input.getPageClass().equals(ListLicenseRecordView.class)) {
                    return LicenseRecordView.class;
                }
                return null;
            }
        });
    }
}
