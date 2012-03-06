package ua.orion.cpu.web.eduprocplanning.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.services.*;
import ua.orion.core.services.EntityService;
import ua.orion.cpu.core.eduprocplanning.entities.Discipline;
import ua.orion.cpu.core.eduprocplanning.entities.EPPCycle;
import ua.orion.cpu.core.eduprocplanning.entities.EduPlan;
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
        //Display EduPlanCycle связывает тип данных EduPlanCycle 
        //со страницей eduprocplanning/PropertyBlocks и идентификатором блока н ней,
        //в котором используется этот тип данных
//        configuration.add(new DisplayBlockContribution("EduPlanCycles", "eduprocplanning/PropertyBlocks", "DisplayEduPlanCycle"));
        //Edit EduPlanCycle
//        configuration.add(new EditBlockContribution("EduPlanCycles", "eduprocplanning/PropertyBlocks", "EditEduPlanCycle"));
        configuration.add(new DisplayBlockContribution("EduPlanTrainingDirection", "eduprocplanning/PropertyBlocks", "DisplayEduPlanTrainingDirection"));
        configuration.add(new DisplayBlockContribution("EduPlanSpeciality",        "eduprocplanning/PropertyBlocks", "DisplayEduPlanSpeciality"));
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

    public static void contributeMetaLinkCoercion(Configuration<Coercion> configuration,
            final EntityService entityService) {
        configuration.add(new Coercion<IMenuLink, Class<EduPlan>>() {

            @Override
            public Class<EduPlan> coerce(IMenuLink input) {
                try {
                    return ("EduProcPlanning/EduPlans".equalsIgnoreCase(input.getPage())
                            || "EduProcPlanning/EduPlan".equalsIgnoreCase(input.getPage()))
                            ? EduPlan.class
                            : null;
                } catch (Exception ex) {
                }
                return null;
            }
        });
    }
}
