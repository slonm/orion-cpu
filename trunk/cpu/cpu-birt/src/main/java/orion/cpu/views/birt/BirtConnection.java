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
    private static Class[] services = new Class[]{Session.class};
    private static Registry registry = null;
    private static Map<String, Object> params = new HashMap<String, Object>();
    public static boolean isServlet = false;
    public static ObjectLocator locator = null;
    public static Session session=null;

    /**
     * Возвращает карту параметров генератору отчетов
     * Если запуск произошел в режиме аплета, то создает реестр
     */
    public static Map<String, Object> params() {
    	params.clear();
    	if (isServlet) {
            for (Class<?> clasz : services) {
                params.put(clasz.getSimpleName(), locator.getService(clasz));
            }
            locator.getService(ExtendedAuthorizer.class).storeUserAndRole(User.SYSTEM_USER, null);
            params.put(User.class.getSimpleName(), locator.getService(ExtendedAuthorizer.class).getUser());
        }else{
        	if(session==null){
        		session=HibernateUtil.getSession();
        	}
            params.put(Session.class.getSimpleName(), session);
            params.put(User.class.getSimpleName(), User.SYSTEM_USER);
        }
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
    }
}
