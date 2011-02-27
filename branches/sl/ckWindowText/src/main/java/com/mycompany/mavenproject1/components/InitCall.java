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
 *
 * @author sl
 */
@SupportsInformalParameters
public class InitCall {

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String method;
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private boolean html;
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
            for(String name: resources.getInformalParameterNames()){
                options.put(name, resources.getInformalParameter(name, String.class));
            }
            javascriptSupport.addInitializerCall(method, options);
        }
        return false;
    }
}
