package orion.cpu.views.tapestry.services;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.ioc.*;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.entities.org.*;
import orion.tapestry.menu.lib.IMenuLink;
import orion.tapestry.menu.lib.PageMenuLink;

/*
 * Модуль конфигурирования IOC
 */
public class UnitsTapestryIOCModule {
    /**
     * Add menu item to configuration
     * @param configuration
     * @param pageLinkCreatorFactory
     */
    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>Units>2Chair";
        configuration.add(path, mlb.buildListPageMenuLink(Chair.class, path));

        path = "Start>Units>1Institute";
        configuration.add(path, mlb.buildListPageMenuLink(Institute.class, path));

        path = "Start>Units>3Department";
        configuration.add(path, mlb.buildListPageMenuLink(Department.class, path));
    }
    private static IMenuLink createPageMenuLink(TapestryCrudModuleService tcms, Class<?> entity,String path){
        IMenuLink lnk=new PageMenuLink(tcms.getListPageClass(entity), BaseEntity.getFullClassName(entity));
        lnk.setParameterPersistent("menupath", path);
        return lnk;
    }
    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration){
        configuration.add("Units", "classpath:Units.properties");
        configuration.add("UnitsTapestry", "classpath:UnitsTapestry.properties");
    }

}
