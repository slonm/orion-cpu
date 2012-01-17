package orion.tapestry.grid.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import orion.tapestry.grid.lib.model.property.impl.*;


/**
 * @author Gennadiy Dobrovolsky
 */
public class CpuGridModule {

    public static void bind(ServiceBinder binder){
        binder.bind(GridBeanModelSource.class, GridBeanModelSourceImpl.class);
        binder.bind(GridPropertyModelSource.class,GridPropertyModelSourceImpl.class);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("grid", "orion.tapestry.grid"));
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add("grid-1.0", "orion.tapestry.grid");
    }

    /**
     * Конфигурация по умолчанию, соответствие между типом данных в Java и типом колонки в Grid
     * Add map Class => GridField type
     * @param configuration соответствие между типом данных в Java и типом колонки в Grid
     */
    public static void contributeGridPropertyModelSource(MappedConfiguration<String, Class> configuration) {
        configuration.add("java.lang.String", GridPropertyModelString.class);

        configuration.add("byte", GridPropertyModelByte.class);
        configuration.add("java.lang.Byte", GridPropertyModelByte.class);

        configuration.add("short", GridPropertyModelShort.class);
        configuration.add("java.lang.Short", GridPropertyModelShort.class);

        configuration.add("int", GridPropertyModelInteger.class);
        configuration.add("java.lang.Integer", GridPropertyModelInteger.class);

        configuration.add("long", GridPropertyModelLong.class);
        configuration.add("java.lang.Long", GridPropertyModelLong.class);

        configuration.add("float", GridPropertyModelFloat.class);
        configuration.add("java.lang.Float", GridPropertyModelFloat.class);

        configuration.add("double", GridPropertyModelDouble.class);
        configuration.add("java.lang.Double", GridPropertyModelDouble.class);

        configuration.add("java.util.Date", GridPropertyModelDate.class);

        configuration.add("boolean", GridPropertyModelBoolean.class);
        configuration.add("java.lang.Boolean", GridPropertyModelBoolean.class);
    }

}
