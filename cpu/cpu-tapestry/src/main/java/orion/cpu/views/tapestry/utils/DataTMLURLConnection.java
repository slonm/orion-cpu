package orion.cpu.views.tapestry.utils;

import orion.tapestry.utils.DataURLConnection;
import br.com.arsmachina.module.service.ControllerSource;
import java.io.IOException;
import java.net.URL;
import orion.cpu.baseentities.BaseEntity;
import orion.cpu.controllers.NamedEntityController;
import orion.cpu.entities.sys.PageTemplate;

/**
 * URLConnection по протоколу data для хоста tml
 * @author sl
 */
public class DataTMLURLConnection extends DataURLConnection{

    private final NamedEntityController<PageTemplate> controller;

    public DataTMLURLConnection(URL url, ControllerSource controllerSource) {
        super(url);
        this.controller = (NamedEntityController<PageTemplate>)(Object)controllerSource.get(PageTemplate.class);
    }

    @Override
    protected BaseEntity<?> getEntity(String name) {
        return controller.findByNameFirst(getFile(url.getFile()));
    }

    @Override
    protected byte[] getInputBuffer() throws IOException {
        if (entity == null) {
            throw new PageTemplateNotFoundException("Template for " + url.getFile() + " not found");
        }
        return ((PageTemplate)entity).getBody().getBytes("UTF8");
    }

    @Override
    public long getLastModified() {
        try {
            connect();
            return ((PageTemplate)entity).getModifyDateTime().getTime();
        } catch (Exception ex) {
            return 0;
        }
    }
}
