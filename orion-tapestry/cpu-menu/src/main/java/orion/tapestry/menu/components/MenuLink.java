/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuLink extends AbstractLink {

    /**
     * The logical name of the page to link to.
     */
    @Parameter(required = false, allowNull = true, defaultPrefix = BindingConstants.LITERAL)
    private String pageName;
    /**
     * The exact name of the page class to link to.
     */
    @Parameter(required = false, allowNull = true, defaultPrefix = BindingConstants.PROP)
    private Class<?> pageClass;
    /**
     * The exact name of the page class to link to.
     */
    @Parameter(required = false, allowNull = true, defaultPrefix = BindingConstants.PROP)
    private Link linkObject;

    void beginRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (linkObject == null) {
            return;
        }

        writeLink(writer, linkObject);
    }

    void afterRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (linkObject == null) {
            return;
        }
        writer.end(); // <a>
    }
}
