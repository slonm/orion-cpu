package orion.tapestry.grid.services;


import java.util.Map;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.services.LibraryMapping;
import orion.tapestry.grid.lib.field.GridFieldAbstract;
import orion.tapestry.grid.lib.field.impl.GridFieldDate;
import orion.tapestry.grid.lib.field.impl.GridFieldNumberByte;
import orion.tapestry.grid.lib.field.impl.GridFieldNumberDouble;
import orion.tapestry.grid.lib.field.impl.GridFieldNumberFloat;
import orion.tapestry.grid.lib.field.impl.GridFieldNumberInteger;
import orion.tapestry.grid.lib.field.impl.GridFieldNumberLong;
import orion.tapestry.grid.lib.field.impl.GridFieldNumberShort;
import orion.tapestry.grid.lib.field.impl.GridFieldString;

/**
 * @author Gennadiy Dobrovolsky
 */
public class CpuGridModule {
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("grid", "orion.tapestry.grid"));
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
        configuration.add("grid-1.0", "orion.tapestry.grid");
    }





    
    /**
     * Создаёт сервис-контейнер,
     * хранящий соответствие между типом данных в Java
     * и типом колонки в Grid
     * @param configuration  registry of field types
     * @return GridTypeMap object
     */
    @ServiceId("TypeMap")
    public static GridTypeMap buildTypeMap(Map<String, Class> configuration) {
        return new GridTypeMapImpl(configuration);
    }



    /**
     * Конфигурация по умолчанию, соответствие между типом данных в Java и типом колонки в Grid
     * Add map Class => GridField type
     * @param configuration соответствие между типом данных в Java и типом колонки в Grid
     */
    public static void contributeTypeMap(MappedConfiguration<String ,Class<? extends GridFieldAbstract> > configuration) {
        configuration.add("java.lang.String" , GridFieldString.class);

        configuration.add("byte"             , GridFieldNumberByte.class);
        configuration.add("java.lang.Byte"   , GridFieldNumberByte.class);

        configuration.add("short"            , GridFieldNumberShort.class);
        configuration.add("java.lang.Short"  , GridFieldNumberShort.class);

        configuration.add("int"              , GridFieldNumberInteger.class);
        configuration.add("java.lang.Integer", GridFieldNumberInteger.class);

        configuration.add("long"             , GridFieldNumberLong.class);
        configuration.add("java.lang.Long"   , GridFieldNumberLong.class);

        configuration.add("float"            , GridFieldNumberFloat.class);
        configuration.add("java.lang.Float"  , GridFieldNumberFloat.class);

        configuration.add("double"           , GridFieldNumberDouble.class);
        configuration.add("java.lang.Double" , GridFieldNumberDouble.class);

        configuration.add("java.util.Date"   , GridFieldDate.class);
    }
}
