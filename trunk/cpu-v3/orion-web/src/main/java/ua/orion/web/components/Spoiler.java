package ua.orion.web.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * 
 * @author sl
 */
@SuppressWarnings("unused")
@Import(library="Spoiler.js", stylesheet="Spoiler.css")
public class Spoiler {

    @Parameter(value="true")
    @Property(write = false)
    private boolean showToggle;
    
    @Parameter(value="")
    @Property(write = false)
    private String link;
}
