package orion.tapestry.menu.services;

import org.apache.tapestry5.services.PageRenderLinkSource;
import orion.tapestry.menu.lib.DefaultLinkCreator;

/**
 * Фабрика создателей ссылок DefaultLinkCreator
 * @author sl
 */
public class DefaultLinkCreatorFactory {

    private final String pageName;
    private final PageRenderLinkSource linkSource;

    public DefaultLinkCreatorFactory(String pageName, PageRenderLinkSource linkSource) {
        this.pageName = pageName;
        this.linkSource = linkSource;
    }

    public DefaultLinkCreator create(String _path) {
        return new DefaultLinkCreator(_path, pageName, linkSource);
    }
}
