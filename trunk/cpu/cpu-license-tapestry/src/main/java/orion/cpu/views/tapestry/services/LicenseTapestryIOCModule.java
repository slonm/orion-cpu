package orion.cpu.views.tapestry.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.ref.EducationForm;
import orion.cpu.entities.ref.EducationalQualificationLevel;
import orion.cpu.entities.ref.KnowledgeAreaOrTrainingDirection;
import orion.cpu.entities.ref.TrainingDirectionOrSpeciality;
import orion.cpu.entities.uch.License;
import orion.cpu.entities.uch.LicenseRecord;
import orion.cpu.entities.uch.LicenseRecordView;
import orion.cpu.views.tapestry.pages.ListView;
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
        //Edit
        configuration.add(new BeanBlockContribution("TrainingDirectionOrSpeciality", "cpu/PropertyBlocks", "EditTrainingDirectionOrSpeciality", true));
        configuration.add(new BeanBlockContribution("KnowledgeAreaOrTrainingDirection", "cpu/PropertyBlocks", "EditKnowledgeAreaOrTrainingDirection", true));
        //Display
        configuration.add(new BeanBlockContribution("TrainingDirectionOrSpeciality", "cpu/PropertyBlocks", "DisplayTrainingDirectionOrSpeciality", false));
        configuration.add(new BeanBlockContribution("KnowledgeAreaOrTrainingDirection", "cpu/PropertyBlocks", "DisplayKnowledgeAreaOrTrainingDirection", false));
    }

    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration) {
        String path;

        path = "Start>License";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(License.class)));

        path = "Start>LicenseRecord";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(LicenseRecord.class)));

        path = "Start>LicenseRecordView";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(LicenseRecordView.class)));

        path = "Start>LicenseRecordView>EducationForm";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(EducationForm.class)));

        path = "Start>LicenseRecordView>EducationalQualificationLevel";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(EducationalQualificationLevel.class)));

        path = "Start>LicenseRecordView>KnowledgeAreaOrTrainingDirection";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(KnowledgeAreaOrTrainingDirection.class)));

        path = "Start>LicenseRecordView>TrainingDirectionOrSpeciality";
        configuration.add(path, new PageMenuLink(ListView.class, BaseEntity.getFullClassName(TrainingDirectionOrSpeciality.class)));
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration){
        configuration.add("License", "classpath:License.properties");
    }
}
