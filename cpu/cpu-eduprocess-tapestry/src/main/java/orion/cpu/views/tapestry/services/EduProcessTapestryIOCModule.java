package orion.cpu.views.tapestry.services;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.ioc.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.ref.Discipline;
import orion.cpu.entities.ref.EPPCycle;
import orion.cpu.entities.ref.Qualification;
import orion.cpu.entities.uch.EduPlan;
import orion.cpu.entities.uch.EduPlanDiscipline;
import orion.cpu.entities.uch.EduPlanDisciplineCycle;
import orion.cpu.entities.uch.EduPlanRecord;
import orion.cpu.entities.uch.EduPlanSemester;
import orion.tapestry.menu.lib.IMenuLink;
import orion.tapestry.menu.lib.PageMenuLink;

/**
 * Модуль конфигурирования IOC
 */
public class EduProcessTapestryIOCModule {

    /**
     * Регистрация блоков для автоформирования моделей данных
     * @param configuration
     * @author sl
     */
//    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
//        //Edit
//        configuration.add(new BeanBlockContribution("TrainingDirectionOrSpeciality", "cpu/PropertyBlocks", "EditTrainingDirectionOrSpeciality", true));
//        configuration.add(new BeanBlockContribution("KnowledgeAreaOrTrainingDirection", "cpu/PropertyBlocks", "EditKnowledgeAreaOrTrainingDirection", true));
//        //Display
//        configuration.add(new BeanBlockContribution("TrainingDirectionOrSpeciality", "cpu/PropertyBlocks", "DisplayTrainingDirectionOrSpeciality", false));
//        configuration.add(new BeanBlockContribution("KnowledgeAreaOrTrainingDirection", "cpu/PropertyBlocks", "DisplayKnowledgeAreaOrTrainingDirection", false));
//    }

    /**
     * Add menu item to configuration
     * @param configuration
     * @param mlb
     */
    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>EduProcess>EduPlan";
        configuration.add(path, mlb.buildListPageMenuLink(EduPlan.class, path));

        path = "Start>EduProcess>EduPlanDiscipline";
        configuration.add(path, mlb.buildListPageMenuLink(EduPlanDiscipline.class, path));

        path = "Start>EduProcess>EduPlanDisciplineCycle";
        configuration.add(path, mlb.buildListPageMenuLink(EduPlanDisciplineCycle.class, path));

        path = "Start>EduProcess>EduPlanRecord";
        configuration.add(path, mlb.buildListPageMenuLink(EduPlanRecord.class, path));

        path = "Start>EduProcess>EduPlanSemester";
        configuration.add(path, mlb.buildListPageMenuLink(EduPlanSemester.class, path));

        path = "Start>EduProcess>Reference>Qualification";
        configuration.add(path, mlb.buildListPageMenuLink(Qualification.class, path));

        path = "Start>EduProcess>Reference>EPPCycle";
        configuration.add(path, mlb.buildListPageMenuLink(EPPCycle.class, path));

        path = "Start>EduProcess>Reference>Discipline";
        configuration.add(path, mlb.buildListPageMenuLink(Discipline.class, path));
    }

    private static IMenuLink createPageMenuLink(TapestryCrudModuleService tcms, Class<?> entity, String path) {
        IMenuLink lnk = new PageMenuLink(tcms.getListPageClass(entity), BaseEntity.getFullClassName(entity));
        lnk.setParameterPersistent("menupath", path);
        return lnk;
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("EduProcess", "classpath:EduProcess.properties");
        configuration.add("EduProcessTapestry", "classpath:EduProcessTapestry.properties");
    }

//    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration) {
//        configuration.add(new Coercion<IMenuLink, Class<LicenseRecordView>>() {
//
//            @Override
//            public Class<LicenseRecordView> coerce(IMenuLink input) {
//                if (input.getPageClass().equals(ListEduProcessRecordView.class)) {
//                    return LicenseRecordView.class;
//                }
//                return null;
//            }
//        });
//    }
}
