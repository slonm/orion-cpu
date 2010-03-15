/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import java.util.ArrayList;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.PageRenderLinkSource;

/**
 *
 * @author Root
 */
public class PageLinkCreator implements LinkCreator {

    /**
     * Page class object
     */
    private String pageName;
    private final Object[] p;
    private final PageRenderLinkSource linkSource;

    public PageLinkCreator(String _pageName, PageRenderLinkSource linkSource,
            Object ... _p) {
        this.pageName = _pageName;
        this.p = _p;
        this.linkSource = linkSource;
    }

    @Override
    public Link create(Object ... context) {
        ArrayList x = new ArrayList();
        if (p == null) {
            return linkSource.createPageRenderLinkWithContext(pageName, context);
        } else {
            for (Object i : p) {
                x.add(i);
            }
            if (context != null) {
                for (Object i : context) {
                    x.add(i);
                }
            }
            return linkSource.createPageRenderLinkWithContext(pageName, x.toArray());
        }
    }
}
