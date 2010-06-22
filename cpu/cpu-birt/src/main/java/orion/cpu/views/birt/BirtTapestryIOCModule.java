package orion.cpu.views.birt;

import br.com.arsmachina.module.service.ControllerSource;
import java.net.URLStreamHandler;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.entities.sys.ReportTemplate;

public class BirtTapestryIOCModule {

    public static void contributeIgnoredPathsFilter(Configuration<String> configuration) {
        configuration.add("/frameset*");
//	      configuration.add("/document*");
//	      configuration.add("/download*");
//	      configuration.add("/parameter*");
//	      configuration.add("/extract*"); 
//	      configuration.add("/run*");
        configuration.add("/preview*");
//	      configuration.add("/output*");      

    }

    public static void contributeURLStreamHandlerFactory(MappedConfiguration<String, URLStreamHandler> configuration,
            ControllerSource controllerSource) {
        NamedEntityController<ReportTemplate> controller = (NamedEntityController<ReportTemplate>) (Object) controllerSource.get(ReportTemplate.class);
        configuration.add("rptdesign", new RptDesignURLStreamHandler(controller));
    }

    public static void contributeRegistryStartup(OrderedConfiguration<Runnable> configuration) {
        configuration.addInstance("CpuBirtInitializeDatabase", CpuBirtInitializeDatabase.class, "after:InitializeDatabase");
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

}
