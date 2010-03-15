/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.PageRenderLinkSource;
import orion.tapestry.menu.lib.LinkCreator;
import orion.tapestry.menu.pages.Navigator;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class CpuMenuModule {

    public static final String MENU_NAVIGATOR="menu.navigator";
    public static final String LIB_NAME="menu";

    /**
     * CpuMenu Service builder
     * @param binder 
     */
    public static void bind(ServiceBinder binder) {
        binder.bind(CpuMenu.class, CpuMenuImpl.class);
        binder.bind(PageLinkCreatorFactory.class);
    }

    public DefaultLinkCreatorFactory buildDefaultLinkFactory(@Inject @Symbol(MENU_NAVIGATOR) String pageName,
            PageRenderLinkSource linkSource){
        return new DefaultLinkCreatorFactory(pageName, linkSource);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping(LIB_NAME, "orion.tapestry.menu"));
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add("menu/1.0", "orion/tapestry/menu");
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(MENU_NAVIGATOR, LIB_NAME+"/"+Navigator.class.getSimpleName());
    }

    /**
     * Add menu item to configuration
     * @param configuration
     * @param linkFactory 
     */
    public static void contributeCpuMenu(MappedConfiguration<String, LinkCreator> configuration,
            DefaultLinkCreatorFactory linkFactory) {
        configuration.add("Start", linkFactory.create("Start"));
    }
}
