/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import orion.tapestry.menu.lib.IMenuLink;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import test.menu.pages.Index;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuLink extends AbstractLink {

    //    /**
    //     * The logical name of the page to link to.
    //     */
    //    @Parameter(required = false, allowNull = true, defaultPrefix = BindingConstants.LITERAL)
    //    private String pageName;
    //
    //    /**
    //     * The exact name of the page class to link to.
    //     */
    //    @Parameter(required = false, allowNull = true, defaultPrefix = BindingConstants.PROP)
    //    private Class<?> pageClass;
    /**
     * The exact name of the page class to link to.
     */
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.PROP)
    private IMenuLink menuLink;
    @Inject
    private PageRenderLinkSource LinkSource;


    void beginRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (menuLink == null) {
            return;
        }

        //Logger logger = LoggerFactory.getLogger(MenuLink.class);
        //logger.info(menuLink + " - class ");

        Link linkObject;
        Object[] context = menuLink.getContext();
        //System.out.println("menuLink.getContext()------"+menuLink.getPageClass());
        if (context == null) {
            linkObject = LinkSource.createPageRenderLink(menuLink.getPageClass());
        } else {
            linkObject = LinkSource.createPageRenderLinkWithContext(menuLink.getPageClass(), context);
        }
        //linkObject = LinkSource.createPageRenderLink(Index.class);
        writeLink(writer, linkObject);
    }

    void afterRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (menuLink == null) {
            return;
        }
        writer.end(); // <a>
    }
}
