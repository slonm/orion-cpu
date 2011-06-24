package ua.orion.cpu.web.eduprocplanning.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.eduprocplanning.entities.Discipline;
import ua.orion.cpu.core.eduprocplanning.entities.EPPCycle;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDisciplineCycle;
import ua.orion.cpu.core.eduprocplanning.entities.Qualification;
import ua.orion.cpu.core.orgunits.entities.*;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

/**
 * Модуль конфигурирования IOC
 */
public class EduProcPlanningWebIOCModule {

//    /**
//     * Регистрация блоков для автоформирования моделей данных
//     * @param configuration
//     * @author sl
//     */
//    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
//        //Display TDS or KATD
//        configuration.add(new DisplayBlockContribution("TrainingDirectionOrSpeciality", "licensing/PropertyBlocks", "DisplayTrainingDirectionOrSpeciality"));
//        configuration.add(new DisplayBlockContribution("KnowledgeAreaOrTrainingDirection", "licensing/PropertyBlocks", "DisplayKnowledgeAreaOrTrainingDirection"));
//        //Edit TDS or KATD
//        configuration.add(new EditBlockContribution("TrainingDirectionOrSpeciality", "licensing/PropertyBlocks", "EditTrainingDirectionOrSpeciality"));
//        configuration.add(new EditBlockContribution("KnowledgeAreaOrTrainingDirection", "licensing/PropertyBlocks", "EditKnowledgeAreaOrTrainingDirection"));
//        //Display EduFormLicenseQuantity
//        configuration.add(new DisplayBlockContribution("EduFormLicenseQuantity", "licensing/PropertyBlocks", "DisplayEduFormLicenseQuantity"));
//        //Edit EduFormLicenseQuantity
//        configuration.add(new EditBlockContribution("EduFormLicenseQuantity", "licensing/PropertyBlocks", "EditEduFormLicenseQuantity"));
//    }
    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;
        
        path = "Start>EduPlanning>EduPlan";
        configuration.add(path, mlb.buildCrudPageMenuLink(EduPlan.class, path));

        path = "Start>EduPlanning>EduPlanDisciplineCycle";
        configuration.add(path, mlb.buildCrudPageMenuLink(EduPlanDisciplineCycle.class, path));

        path = "Start>EduPlanning>>Reference";
        
        path = "Start>EduPlanning>>Reference>EPPCycle";
        configuration.add(path, mlb.buildCrudPageMenuLink(EPPCycle.class, path));
        
        path = "Start>EduPlanning>>Reference>Discipline";
        configuration.add(path, mlb.buildCrudPageMenuLink(Discipline.class, path));
        
        path = "Start>EduPlanning>>Reference>Qualification";
        configuration.add(path, mlb.buildCrudPageMenuLink(Qualification.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("eduprocplanning", "ua.orion.cpu.web.eduprocplanning"));
    }
}
