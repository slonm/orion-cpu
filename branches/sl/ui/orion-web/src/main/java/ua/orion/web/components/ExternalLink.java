/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.components;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractComponentEventLink;

/**
 *
 * @author slobodyanuk
 */
public class ExternalLink extends AbstractComponentEventLink {

    @Parameter(required=true, allowNull=false)
    private Link link;

    @Override
    protected Link createLink(Object[] eventContext) {
        return link;
    }
}
