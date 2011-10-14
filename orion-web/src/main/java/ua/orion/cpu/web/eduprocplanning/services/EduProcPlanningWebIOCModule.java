package ua.orion.cpu.web.eduprocplanning.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.eduprocplanning.entities.Discipline;
import ua.orion.cpu.core.eduprocplanning.entities.EPPCycle;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlanDisciplineCycle;
import ua.orion.cpu.core.eduprocplanning.entities.Qualification;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

/**
 * Модуль конфигурирования IOC
 */
public class EduProcPlanningWebIOCModule {

    /**
     * Регистрация блоков для автоформирования моделей данных
     * @param configuration
     * @author sl
     */
    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
        //Display EduPlanDisciplineCycle связывает тип данных EduPlanDisciplineCycle 
        //со страницей eduprocplanning/PropertyBlocks и идентификатором блока н ней,
        //в котором используется этот тип данных
        configuration.add(new DisplayBlockContribution("EduPlanDisciplineCycles", "eduprocplanning/PropertyBlocks", "DisplayEduPlanDisciplineCycle"));
        //Edit EduPlanDisciplineCycle
        configuration.add(new EditBlockContribution("EduPlanDisciplineCycles", "eduprocplanning/PropertyBlocks", "EditEduPlanDisciplineCycle"));
    }
    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;
        
        path = "Start>EduProcPlanning>EduPlan";
        configuration.add(path, mlb.buildCrudPageMenuLink(EduPlan.class, path));

        path = "Start>EduProcPlanning>EduPlanDisciplineCycle";
        configuration.add(path, mlb.buildCrudPageMenuLink(EduPlanDisciplineCycle.class, path));

        path = "Start>EduProcPlanning>Reference";
        
        path = "Start>EduProcPlanning>Reference>EPPCycle";
        configuration.add(path, mlb.buildCrudPageMenuLink(EPPCycle.class, path));
        
        path = "Start>EduProcPlanning>Reference>Discipline";
        configuration.add(path, mlb.buildCrudPageMenuLink(Discipline.class, path));
        
        path = "Start>EduProcPlanning>Reference>Qualification";
        configuration.add(path, mlb.buildCrudPageMenuLink(Qualification.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("eduprocplanning", "ua.orion.cpu.web.eduprocplanning"));
    }
}
