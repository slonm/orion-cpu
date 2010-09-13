package orion.cpu.views.birt;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import orion.cpu.entities.sys.ReportTemplate;
import orion.cpu.views.tapestry.services.MenuLinkBuilder;
import orion.tapestry.menu.lib.IMenuLink;

public class BirtTapestryIOCModule {

    public static void contributeIgnoredPathsFilter(Configuration<String> configuration) {
        configuration.add("/frameset*");
//	      configuration.add("/document*");
//	      configuration.add("/download*");
//	      configuration.add("/parameter*");
//	      configuration.add("/extract*"); 
//	      configuration.add("/run*");
        configuration.add("/preview*");
	configuration.add("/output*");      

    }

    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration) {
        configuration.addInstance("CpuBirtInitializeDatabase", CpuBirtInitializeDatabase.class, "after:CpuTapestryInitializeDatabase");
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(ReportPreviewLinkFactory.class, ReportPreviewLinkFactoryImpl.class);
    }

    /**
     * Регистрация библиотеки компонентов
     * @param configuration конфигурация
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("birt", "orion.cpu.views.birt"));
    }

    public static void contributeDataURLStreamHandler(MappedConfiguration<String, Class<?>> configuration){
        configuration.add("rptdesign", DataRptDesignURLConnection.class);
    }

    public static void contributeGlobalMessageAppender(OrderedConfiguration<String> configuration) {
        configuration.add("BirtTapestry", "classpath:BirtTapestry.properties");
    }

    public static void contributeCpuMenu(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;
        path = "Start>Admin>Report";
        configuration.add(path, mlb.buildListPageMenuLink(ReportTemplate.class, path));
    }


}
