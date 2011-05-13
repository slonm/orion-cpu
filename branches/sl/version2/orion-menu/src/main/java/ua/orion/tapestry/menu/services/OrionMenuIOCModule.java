/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.services;

import java.util.Map;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.LibraryMapping;
import ua.orion.tapestry.menu.lib.IMenuLink;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class OrionMenuIOCModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(OrionMenuService.class, OrionMenuServiceImpl.class);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("menu", "ua.orion.tapestry.menu"));
    }
}
