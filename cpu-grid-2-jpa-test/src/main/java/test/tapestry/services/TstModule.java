package test.tapestry.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.tynamo.jpa.JPASymbols;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
//Перечень дополнительных модулей
@SubModule({orion.tapestry.grid.services.CpuGridModule.class,orion.tapestry.grid.services.CpuGridJPA.class})
public class TstModule {

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add("tapestry.production-mode", "false");

      configuration.add(JPASymbols.PERSISTENCE_UNIT, "sample");

    }
}
