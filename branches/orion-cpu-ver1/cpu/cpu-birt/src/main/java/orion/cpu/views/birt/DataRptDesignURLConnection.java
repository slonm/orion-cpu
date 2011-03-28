package orion.cpu.views.birt;

import br.com.arsmachina.module.service.ControllerSource;
import java.io.IOException;
import java.net.URL;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.entities.sys.ReportTemplate;
import orion.tapestry.utils.DataURLConnection;
/**
 * URLConnection по протоколу data для хоста tml
 * @author sl
 */
public class DataRptDesignURLConnection extends DataURLConnection{

    private final NamedEntityController<ReportTemplate> controller;

    public DataRptDesignURLConnection(URL url, ControllerSource controllerSource) {
        super(url);
        this.controller = (NamedEntityController<ReportTemplate>)(Object)controllerSource.get(ReportTemplate.class);
    }

    @Override
    protected BaseEntity<?> getEntity(String name) {
        return controller.findByNameFirst(getFile(url.getFile()));
    }

    @Override
    protected byte[] getInputBuffer() throws IOException {
        if (entity == null) {
            throw new ReportTemplateNotFoundException("Template for " + url.getFile() + " not found");
        }
        return ((ReportTemplate)entity).getBody().getBytes("UTF8");
    }

}
