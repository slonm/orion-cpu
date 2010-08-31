package orion.cpu.views.tapestry.services;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
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
            TapestryCrudModuleService tcms) {
        String path;

        path = "Start>Chair";
        configuration.add(path, createPageMenuLink(tcms, Chair.class, path));
    }
    private static IMenuLink createPageMenuLink(TapestryCrudModuleService tcms, Class<?> entity,String path){
        IMenuLink lnk=new PageMenuLink(tcms.getListPageClass(entity), BaseEntity.getFullClassName(entity));
        lnk.setParameterPersistent("menupath", path);
        return lnk;
    }
    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration){
        configuration.add("UnitsTapestry", "classpath:UnitsTapestry.properties");
    }

}
