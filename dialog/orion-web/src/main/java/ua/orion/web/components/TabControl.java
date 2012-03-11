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
    @Parameter(required = true, principal = true, autoconnect = true)
    private Iterable<String> panelTitles;
    /**
     * set the panel with given id as activated.
     */
    @Parameter(value = "0", allowNull = false, required = true)
    private String activePanelId;
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property(write = false)
    private String contentZone;
    @Property
    private int panelId;
    @Environmental
    private JavaScriptSupport javascriptSupport;
    private String assignedClientId;
    private Map<Integer, String> titlesList;

    boolean setupRender() {
        assignedClientId = javascriptSupport.allocateClientId(clientId);
        //if panelTitles is empty do not render TabControl
        if (panelTitles != null && panelTitles.iterator().hasNext()) {
            titlesList = new HashMap();
            Integer i = 0;
            for (String s : panelTitles) {
                titlesList.put(i++, s);
            }
            return true;
        }
        return false;
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer) {
        javascriptSupport.addScript("new TabControl('%s', '%s');",
                getClientId(), getClientId() + "_" + activePanelId);
    }

    public Collection getPanelIds() {
        return titlesList.keySet();
    }

    public String getPanelTitle() {
        return titlesList.get(panelId);
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
