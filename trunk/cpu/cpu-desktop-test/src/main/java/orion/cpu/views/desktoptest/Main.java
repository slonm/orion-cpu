package orion.cpu.views.desktoptest;

import br.com.arsmachina.authentication.entity.User;
import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import orion.cpu.security.services.ExtendedAuthorizer;

public class Main {

    public static void main(String[] args) {

        RegistryBuilder builder = new RegistryBuilder();
        IOCUtilities.addDefaultModules(builder);
        builder.add(AppModule.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();
        ExtendedAuthorizer ea=registry.getService(ExtendedAuthorizer.class);
        ea.storeUserAndRole(User.SYSTEM_USER, null);
        
        new TestController(registry);
        
        //for operations done from this thread
        registry.cleanupThread();
        //call this to allow services clean shutdown
        registry.shutdown();
    }
}
