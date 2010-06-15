package orion.cpu.views.tapestry.services;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.Coercion;
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
import orion.cpu.views.tapestry.pages.uch.ListLicenseRecordView;
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
    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            TapestryCrudModuleService tcms) {
        String path;

        path = "Start>License";
        configuration.add(path, createPageMenuLink(tcms, License.class));

        path = "Start>LicenseRecord";
        configuration.add(path, createPageMenuLink(tcms, LicenseRecord.class));

        path = "Start>LicenseRecordView";
        configuration.add(path, new PageMenuLink(tcms.getListPageClass(LicenseRecordView.class)));

        path = "Start>LicenseRecordView>EducationForm";
        configuration.add(path, createPageMenuLink(tcms, EducationForm.class));

        path = "Start>LicenseRecordView>EducationalQualificationLevel";
        configuration.add(path, createPageMenuLink(tcms, EducationalQualificationLevel.class));

        path = "Start>LicenseRecordView>KnowledgeAreaOrTrainingDirection";
        configuration.add(path, createPageMenuLink(tcms, KnowledgeAreaOrTrainingDirection.class));

        path = "Start>LicenseRecordView>TrainingDirectionOrSpeciality";
        configuration.add(path, createPageMenuLink(tcms, TrainingDirectionOrSpeciality.class));
    }
    private static PageMenuLink createPageMenuLink(TapestryCrudModuleService tcms, Class<?> entity){
        return new PageMenuLink(tcms.getListPageClass(entity), BaseEntity.getFullClassName(entity));
    }
    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration){
        configuration.add("License", "classpath:License.properties");
    }

    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration) {
        configuration.add(new Coercion<IMenuLink, Class<LicenseRecordView>>(){

            @Override
            public Class<LicenseRecordView> coerce(IMenuLink input) {
                if(input.getPageClass().equals(ListLicenseRecordView.class))
                    return LicenseRecordView.class;
                return null;
            }
        });
    }
}
