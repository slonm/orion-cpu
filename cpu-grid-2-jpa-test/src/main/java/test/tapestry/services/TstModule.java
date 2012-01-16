package test.tapestry.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.jpa.JpaSymbols;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
//Перечень дополнительных модулей
@SubModule({orion.tapestry.grid.services.CpuGridModule.class, orion.tapestry.grid.services.CpuGridJPA.class})
public class TstModule {

//    @Contribute(SymbolProvider.class)
//    @FactoryDefaults
//    public static void provideFactoryDefaults(final MappedConfiguration<String, String> configuration) {
//        configuration.add(JpaSymbols.PERSISTENCE_DESCRIPTOR, "persistence.xml");
//    }
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add("tapestry.production-mode", "false");
    }
    
    
}
