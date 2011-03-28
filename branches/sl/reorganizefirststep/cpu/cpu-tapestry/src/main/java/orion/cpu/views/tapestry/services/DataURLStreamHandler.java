package orion.cpu.views.tapestry.services;

import br.com.arsmachina.module.service.ControllerSource;
import java.io.*;
import java.net.*;
import java.util.Map;
import orion.cpu.baseentities.BaseEntity;
import orion.tapestry.utils.DataURLConnection;

/**
 * URLStreamHandler for <code>data</code> protocol
 * URL выглядит следующим образом: data://<тип данных>/<имя класса страницы>.tml
 * <тип данных> передается в конфигурации сервиса
 * См. статью http://www.skipy.ru/useful/protocolHandler.html
 * @author sl
 */
public class DataURLStreamHandler extends URLStreamHandler {

    private final ControllerSource controllerSource;
    private final Map<String, Class> configuaration;

    public DataURLStreamHandler(ControllerSource controllerSource,
            Map<String, Class> configuaration) {
        this.controllerSource = controllerSource;
        this.configuaration = configuaration;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        Class<?> clasz = configuaration.get(url.getHost());
        try {
            return (URLConnection) clasz.getConstructors()[0].newInstance(url, controllerSource);
        } catch (Exception ex) {
            return new DataURLConnection(url) {

                @Override
                protected Object getEntity(String name) {
                    return null;
                }

                @Override
                protected byte[] getInputBuffer() throws IOException {
                    return null;
                }
            };
        }
    }
}
