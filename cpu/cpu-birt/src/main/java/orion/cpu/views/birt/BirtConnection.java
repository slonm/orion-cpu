package orion.cpu.views.birt;

import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.module.service.ControllerSource;
import java.util.*;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.hibernate.Session;
import org.slf4j.*;
import orion.cpu.security.services.ExtendedAuthorizer;

/**
 * Статический класс для связи между IOC и сервлетом или аплетом ReportViewer
 */
public class BirtConnection {

    static final private Logger LOG = LoggerFactory.getLogger(BirtConnection.class);
    private static Class[] services = new Class[]{ControllerSource.class, SymbolSource.class, ExtendedAuthorizer.class, Session.class};
    private static Registry registry = null;
    private static Map<String, Object> params = new HashMap<String, Object>();
    public static boolean isServlet = false;
    public static ObjectLocator locator = null;

    /**
     * Добавляет в карту параметров основные сервисы для работы с данными и текущего пользователя
     */
    private static void createParams(Map<String, Object> params) {
        params.clear();
        for (Class<?> clasz : services) {
            params.put(clasz.getSimpleName(), locator.getService(clasz));
        }
        params.put(User.class.getSimpleName(), locator.getService(ExtendedAuthorizer.class).getUser());
    }

    /**
     * Возвращает карту параметров генератору отчетов
     * Если запуск произошел в режиме аплета, то создает реестр
     */
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

    /**
     * Возвращает карту параметров генератору отчетов, добавляя при этом параметры
     * полученные в _params
     * Если запуск произошел в режиме аплета, то создает реестр
     */
    public static Map<String, Object> params(Map<String, Object> _params) {
        params().putAll(_params);
        return params;
    }

    /**
     * Должен вызыватся при завершении работы генератора отчетов
     */
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
