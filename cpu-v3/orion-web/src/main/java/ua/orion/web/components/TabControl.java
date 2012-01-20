/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.components;

import java.util.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Simple tab controlled panel component.
 * <p/>
 * This component looks in the container message resource for an entry like <em>label-panelid</em> for inserting as panel title.
 * If key not found the panel id inserted instead.
 *
 * 
 */
@Import(library = {"TabControl.js"}, stylesheet = {"TabControl.css"})
public class TabControl {

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;
    /**
     * list of labels (for each panel).
     */
    @Parameter(required = true, defaultPrefix = "list")
    private List<String> panelTitles;
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private int tabcount;
    /**
     * set the panel with given id as activated.
     */
    @Parameter(value = "0", allowNull = false, required = true)
    private String activePanelId;
    @Parameter(defaultPrefix = "literal")
    @Property(write = false)
    private String contentZone;
    @Property
    private int panelId;
    @Environmental
    private JavaScriptSupport javascriptSupport;
    private String assignedClientId;

    void setupRender() {
        assignedClientId = javascriptSupport.allocateClientId(clientId);
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer) {
        javascriptSupport.addScript("new Ori.TabControl('%s', '%s');", 
                getClientId(), getClientId() + "_" + activePanelId);
    }

    public List<String> getPanelIds() {
        List<String> list = new ArrayList();
        for (int i = 0; i < tabcount; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    public String getPanelTitle() {
        if (panelId < panelTitles.size()) {
            return panelTitles.get(panelId);
        }
        return String.valueOf(panelId);
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId() {
        return assignedClientId;
    }
}
