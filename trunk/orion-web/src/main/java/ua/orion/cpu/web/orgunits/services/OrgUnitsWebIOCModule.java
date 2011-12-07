package ua.orion.cpu.web.orgunits.services;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.cpu.core.orgunits.entities.*;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

/**
 * Модуль конфигурирования IOC
 */
public class OrgUnitsWebIOCModule {

//    /**
//     * Регистрация блоков для автоформирования моделей данных
//     * @param configuration
//     * @author sl
//     */
//    public static void contributeBeanBlockSource(Configuration<BeanBlockContribution> configuration) {
//    }
    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory 
     */
    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>OrgUnits>Institute";
        configuration.add(path, mlb.buildCrudPageMenuLink(Institute.class, path));

        path = "Start>OrgUnits>Chair";
        configuration.add(path, mlb.buildCrudPageMenuLink(Chair.class, path));

        path = "Start>OrgUnits>Department";
        configuration.add(path, mlb.buildCrudPageMenuLink(Department.class, path));

        path = "Start>OrgUnits>OrgUnitPost";
        configuration.add(path, mlb.buildCrudPageMenuLink(OrgUnitPost.class, path));

        path = "Start>OrgUnits>Post";
        configuration.add(path, mlb.buildCrudPageMenuLink(Post.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("orgunits", "ua.orion.cpu.web.orgunits"));
    }
}
