package com.mycompany.mavenproject1.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A non visual component.
 * Adds a call to a client-side function inside the Tapestry.Initializer namespace. 
 * It same as call JavascriptSupport.addInitializerCall method.
 * Calls to this method are aggregated into a call to the Tapestry.init() function. 
 * Initialization occurs at {@link InitializationPriority#NORMAL} priority.
 * All informal parameters has been added as JSON parametrs.
 * @author sl
 */
@SupportsInformalParameters
public class InitCall {

    /**
     * Client-side function inside the Tapestry.Initializer namespace 
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String function;
    /**
     * Add function call at page render request
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private boolean html;
    /**
     * Add function call at component event's Zone update
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private boolean ajax;
    @Environmental
    private JavaScriptSupport javascriptSupport;
    @Inject
    private Request request;
    @Inject
    private ComponentResources resources;

    Object beginRender() {
        if ((request.isXHR() && ajax) || (!request.isXHR() && html)) {
            JSONObject options = new JSONObject();
            for (String name : resources.getInformalParameterNames()) {
                options.put(name, resources.getInformalParameter(name, String.class));
            }
            javascriptSupport.addInitializerCall(function, options);
        }
        return false;
    }
}
