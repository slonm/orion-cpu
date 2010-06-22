package orion.cpu.views.birt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.entities.sys.ReportTemplate;

/**
 * URLStreamHandler for <code>rptdesign</code> protocol
 * URL выглядит следующим образом: rptdesign:///<имя шаблона страницы>
 * См. статью http://www.skipy.ru/useful/protocolHandler.html
 * @author sl
 */
public class RptDesignURLStreamHandler extends URLStreamHandler{

    private final NamedEntityController<ReportTemplate> controller;

    public RptDesignURLStreamHandler(NamedEntityController<ReportTemplate> controller) {
        this.controller = controller;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new TMLURLConnection(url);
    }

    /**
     * URLConnection implementation for <code>tml</code> protocol
     * @author sl
     */
    public class TMLURLConnection extends URLConnection {

        private InputStream is = null;

        public TMLURLConnection(URL url) {
            super(url);
        }

        @Override
        public synchronized void connect() throws IOException {
            if (connected) {
                return;
            }
            try {
                ReportTemplate pt = controller.findByNameFirst(url.getFile());
                is = (pt == null) ? null : new ByteArrayInputStream(pt.getBody().getBytes("UTF8"));
                connected = true;
            } catch (Exception ex) {
            }
        }

        @Override
        public synchronized InputStream getInputStream() throws IOException {
            connect();
            if (is == null) {
                throw new ReportTemplateNotFoundException("Template for " + url.getHost() + " not found");
            }
            return is;
        }

        public boolean exists() {
            ReportTemplate pt = controller.findByNameFirst(url.getFile());
            return pt != null;
        }
    }
}
