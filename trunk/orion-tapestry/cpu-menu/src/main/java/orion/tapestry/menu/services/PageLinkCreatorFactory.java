package orion.tapestry.menu.services;

import org.apache.tapestry5.services.PageRenderLinkSource;
import orion.tapestry.menu.lib.PageLinkCreator;

/**
 * Фабрика создателей ссылок PageLinkCreator
 * @author sl
 */
public class PageLinkCreatorFactory {

    private final PageRenderLinkSource linkSource;

    public PageLinkCreatorFactory(PageRenderLinkSource linkSource) {
        this.linkSource = linkSource;
    }

    public PageLinkCreator create(String path, Object ... context) {
        return new PageLinkCreator(path, linkSource, context);
    }
}
