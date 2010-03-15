/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.PageRenderLinkSource;
import orion.tapestry.menu.pages.Navigator;

/**
 * @author Gennadiy Dobrovolsky
 */
public class DefaultLinkCreator implements LinkCreator {

    /**
     * Если пункт меню не имеет своей ссылки,
     * то этому пункту меню присваиваются ссылка
     * на страницу pageClass
     */
    private final String pageName;
    private final String path;
    private final PageRenderLinkSource linkSource;

    public DefaultLinkCreator(String _path, String pageName, PageRenderLinkSource linkSource) {
        this.path = _path;
        this.linkSource = linkSource;
        this.pageName = pageName;
    }

    @Override
    public Link create(Object... context) {
        return linkSource.createPageRenderLinkWithContext(pageName, path);
    }
}
