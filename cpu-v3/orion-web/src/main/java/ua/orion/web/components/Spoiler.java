package ua.orion.web.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * 
 * @author sl
 */
@SuppressWarnings("unused")
@Import(library = "Spoiler.js", stylesheet = "Spoiler.css")
public class Spoiler {

    @Parameter(value = "true")
    @Property(write = false)
    private boolean showToggle;
    @Parameter(value = "")
    @Property(write = false)
    private String link;
    @Environmental
    private JavaScriptSupport javascriptSupport;
    @Property
    private String clientId;
    @Inject
    private ComponentResources resources;

    void setupRender() {
        clientId = javascriptSupport.allocateClientId(resources);
        javascriptSupport.addInitializerCall("initSpoilerLink", clientId+"-link");
    }
}
