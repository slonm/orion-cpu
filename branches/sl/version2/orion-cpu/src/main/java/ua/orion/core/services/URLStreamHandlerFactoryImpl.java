package ua.orion.core.services;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Map;
import org.apache.tapestry5.ioc.util.CaseInsensitiveMap;

/**
 * Реализация фабрики URLStreamHandlerFactory, которая возвращает URLStreamHandler
 * на основании конфигурации IOC
 * @author sl
 */
public class URLStreamHandlerFactoryImpl implements URLStreamHandlerFactory {

    private final CaseInsensitiveMap<URLStreamHandler> configuration=new CaseInsensitiveMap<URLStreamHandler>();

    public URLStreamHandlerFactoryImpl(Map<String, URLStreamHandler> configuration) {
        this.configuration.putAll(configuration);
    }


    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        return configuration.get(protocol);
    }

}
