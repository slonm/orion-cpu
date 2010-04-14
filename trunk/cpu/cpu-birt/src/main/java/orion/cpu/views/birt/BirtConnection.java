package orion.cpu.views.birt;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.slf4j.*;
import orion.cpu.security.services.ExtendedAuthorizer;

public class BirtConnection {

    static final private Logger LOG = LoggerFactory.getLogger(BirtConnection.class);
    private static Class[] services = new Class[]{ControllerSource.class, SymbolSource.class, ExtendedAuthorizer.class};
    private static Registry registry = null;
    private static Map<String, Object> params = new HashMap<String, Object>();
    public static boolean isServlet = false;
    public static ObjectLocator locator = null;

    private static void createParams(Map<String, Object> params) {
        params.clear();
        for (Class<?> clasz : services) {
            params.put(clasz.getSimpleName(), locator.getService(clasz));
        }
        params.put(User.class.getSimpleName(), locator.getService(ExtendedAuthorizer.class).getUser());
    }

    public static Map<String, Object> params() {
        if (!isServlet) {
            if (registry == null) {
                RegistryBuilder builder = new RegistryBuilder();
                IOCUtilities.addDefaultModules(builder);
                registry = builder.build();
                registry.performRegistryStartup();
                ExtendedAuthorizer ea = registry.getService(ExtendedAuthorizer.class);
                ea.storeUserAndRole(User.SYSTEM_USER, null);
            }
        }
        createParams(params);
        return params;
    }

    public static Map<String, Object> params(Map<String, Object> _params) {
        params().putAll(_params);
        return params;
    }

    public static void shutdown() {
        if (!isServlet) {
            //for operations done from this thread
            registry.cleanupThread();
            //call this to allow services clean shutdown
            registry.shutdown();
            registry = null;
        }
    }
}
