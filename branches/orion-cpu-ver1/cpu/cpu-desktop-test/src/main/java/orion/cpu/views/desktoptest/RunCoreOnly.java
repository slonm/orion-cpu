package orion.cpu.views.desktoptest;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.module.service.ControllerSource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import orion.cpu.security.services.ExtendedAuthorizer;

public class RunCoreOnly {

    //private final static Logger LOG = LoggerFactory.getLogger(RunCoreOnly.class);

    public static Registry startup() {
        RegistryBuilder builder = new RegistryBuilder();
        List<String> modules=TestUtilities.listDefaultModules();
        Iterator<String> it=modules.iterator();
        while(it.hasNext()){
            String module=it.next();
            if(module.startsWith("orion.cpu.views")||
               module.startsWith("br.com.arsmachina.tapestrycrud")||
               module.startsWith("nu.localhost")||
               module.startsWith("orion.tapestry")||
               module.equals("org.apache.tapestry5.beanvalidator.BeanValidatorModule")
               ){
               it.remove();
            }
        }
        for(String module:modules){
            builder.add(module);
        }
        builder.add(AppModule.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();
        ExtendedAuthorizer ea = registry.getService(ExtendedAuthorizer.class);
        ea.storeUserAndRole(User.SYSTEM_USER, null);
        return registry;
    }

    public static void main(String[] args) throws IOException {
	System.setProperty("java.util.logging.config.file", "logging.properties");
        Registry registry = startup();
        
        shutdown(registry);
    }

    public static void shutdown(Registry registry) {
        //for operations done from this thread
        registry.cleanupThread();
        //call this to allow services clean shutdown
        registry.shutdown();
    }
}
