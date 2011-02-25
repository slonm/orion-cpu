/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 *
 * @author slobodyanuk
 */
public class PopupWindowEvent extends EventLink {

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
    private String window;
    @Inject
    private Request request;

    void beforeRenderTemplate(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (!request.isXHR()) {
            writer.getElement().forceAttributes(MarkupConstants.ONCLICK, 
                    String.format("javascript:$('%s').getStorage().ck_window.showCenter(true);return Tapestry.waitForPage(event);", window));
        }
    }
}
