package orion.cpu.views.desktoptest;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.module.service.ControllerSource;
import org.apache.tapestry5.internal.TapestryAppInitializer;
import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.services.TapestryModule;
import org.slf4j.*;
import orion.cpu.security.services.ExtendedAuthorizer;

public class RunAll {

    private final static Logger LOG = LoggerFactory.getLogger(RunAll.class);

    public static Registry startup() {
//        TapestryAppInitializer tai=new TapestryAppInitializer
        RegistryBuilder builder = new RegistryBuilder();
//        IOCUtilities.addDefaultModules(builder);
        builder.add(TapestryModule.class);
        builder.add(AppModule.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();
        ExtendedAuthorizer ea = registry.getService(ExtendedAuthorizer.class);
        ea.storeUserAndRole(User.SYSTEM_USER, null);
        return registry;
    }

    public static void main(String[] args) {
        Registry registry = startup();
        ControllerSource cs = registry.getService(ControllerSource.class);

        shutdown(registry);
    }

    public static void shutdown(Registry registry) {
        //for operations done from this thread
        registry.cleanupThread();
        //call this to allow services clean shutdown
        registry.shutdown();
    }
}
