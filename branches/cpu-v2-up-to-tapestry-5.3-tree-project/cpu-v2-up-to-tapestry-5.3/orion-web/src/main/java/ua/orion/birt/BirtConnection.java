package ua.orion.birt;

import java.util.*;
import javax.persistence.EntityManager;
import org.apache.tapestry5.ioc.*;

/**
 * Статический класс для связи между IOC и сервлетом или аплетом ReportViewer
 */
public class BirtConnection {

    private static Class[] services = new Class[]{EntityManager.class};
    private static Map<String, Object> params = new HashMap<String, Object>();
    private static ObjectLocator LOCATOR = null;

    /**
     * Возвращает карту параметров генератору отчетов
     */
    public static Map<String, Object> params() {
        params.clear();
        for (Class<?> clasz : services) {
            params.put(clasz.getSimpleName(), LOCATOR.getService(clasz));
        }
        return params;
    }

    /**
     * Возвращает карту параметров генератору отчетов, добавляя при этом параметры
     * полученные в _params
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

    public static void setLOCATOR(ObjectLocator locator) {
        BirtConnection.LOCATOR = locator;
    }
    
    
}
