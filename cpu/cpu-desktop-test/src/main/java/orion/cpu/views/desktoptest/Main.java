package orion.cpu.views.desktoptest;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.service.ControllerSource;
import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.slf4j.*;
import orion.cpu.entities.pub.B;
import orion.cpu.security.services.ExtendedAuthorizer;

public class Main {

    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static Registry startup() {
        RegistryBuilder builder = new RegistryBuilder();
        IOCUtilities.addDefaultModules(builder);
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
        Controller<B, Integer> cB = cs.get(B.class);

        B bX = new B("XX");
        cB.save(bX);

//        B bY = new B("YY").alias(bX);
//        cB.save(bY);

//        new TestDAOFindByExample(registry);
//        new TestController(registry);
        shutdown(registry);
    }

    public static void shutdown(Registry registry) {
        //for operations done from this thread
        registry.cleanupThread();
        //call this to allow services clean shutdown
        registry.shutdown();
    }
}
