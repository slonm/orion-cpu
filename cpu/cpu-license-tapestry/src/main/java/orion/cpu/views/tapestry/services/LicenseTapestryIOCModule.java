package orion.cpu.views.tapestry.services;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.ref.*;
import orion.cpu.entities.uch.*;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.impl.GridFieldKnowledgeAreaOrTrainingDirection;
import orion.tapestry.menu.lib.IMenuLink;
import orion.tapestry.menu.lib.PageMenuLink;

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
        //Display TDS or KATD
        configuration.add(new DisplayBlockContribution("TrainingDirectionOrSpeciality", "LicensePropertyBlocks", "DisplayTrainingDirectionOrSpeciality"));
        configuration.add(new DisplayBlockContribution("KnowledgeAreaOrTrainingDirection", "LicensePropertyBlocks", "DisplayKnowledgeAreaOrTrainingDirection"));
        //Edit TDS or KATD
        configuration.add(new EditBlockContribution("TrainingDirectionOrSpeciality", "LicensePropertyBlocks", "EditTrainingDirectionOrSpeciality"));
        configuration.add(new EditBlockContribution("KnowledgeAreaOrTrainingDirection", "LicensePropertyBlocks", "EditKnowledgeAreaOrTrainingDirection"));
        //Display EduFormLicenseQuantity
        configuration.add(new DisplayBlockContribution("EduFormLicenseQuantity", "LicensePropertyBlocks", "DisplayEduFormLicenseQuantity"));
        //Edit EduFormLicenseQuantity
        configuration.add(new EditBlockContribution("EduFormLicenseQuantity", "LicensePropertyBlocks", "EditEduFormLicenseQuantity"));
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

        path = "Start>License>LicenseRecord>KnowledgeAreaOrTrainingDirection";
        configuration.add(path, mlb.buildListPageMenuLink(KnowledgeAreaOrTrainingDirection.class, path));

        path = "Start>License>LicenseRecord>TrainingDirectionOrSpeciality";
        configuration.add(path, mlb.buildListPageMenuLink(TrainingDirectionOrSpeciality.class, path));

        path = "Start>License>LicenseRecord>LicenseRecordGroup";
        configuration.add(path, mlb.buildListPageMenuLink(LicenseRecordGroup.class, path));
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("License", "classpath:License.properties");
        configuration.add("LicenseTapestry", "classpath:LicenseTapestry.properties");
    }

    /**
     * Связываем класс сущности KnowledgeAreaOrTrainingDirection
     * и класс для её отображения в Grid
     * @author Gennadiy Dobrovolsky
     */
      public static void contributeTypeMap(MappedConfiguration<String, Class<? extends GridFieldAbstract>> configuration) {
          configuration.add(KnowledgeAreaOrTrainingDirection.class.getName(), GridFieldKnowledgeAreaOrTrainingDirection.class);
      }
}
