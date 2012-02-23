package ua.orion.birt;

import ua.orion.birt.ReportTemplateNotFoundException;
import ua.orion.birt.entities.ReportTemplate;
import java.io.IOException;
import java.net.URL;
import ua.orion.birt.entities.ReportTemplate;
import ua.orion.core.DataURLConnection;
import ua.orion.core.services.EntityService;

/**
 * URLConnection по протоколу data для хоста tml
 * @author sl
 */
public class DataRptDesignURLConnection extends DataURLConnection {

    private final EntityService es;

    public DataRptDesignURLConnection(URL url, EntityService es) {
        super(url);
        this.es = es;
    }

    @Override
    protected Object getEntity(String name) {
        return es.findByUKey(ReportTemplate.class, getFile(url.getFile()));
    }

    @Override
    protected byte[] getInputBuffer() throws IOException {
        if (entity == null) {
            throw new ReportTemplateNotFoundException("Template for " + url.getFile() + " not found");
        }
        return ((ReportTemplate) entity).getBody().getBytes("UTF8");
    }
}
