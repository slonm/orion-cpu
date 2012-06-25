package ua.orion.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Simple tab controlled panel component.
 * <p/>
 */
public class TabControl {

    /**
     * The id used to generate a page-unique client-side identifier for the
     * component. If a component renders multiple times, a suffix will be
     * appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;
    /**
     * list of labels (for each panel).
     */
    @Parameter(required = true, principal = true, autoconnect = true, allowNull = false)
    @Property
    private Iterable<String> tabTitles;
    /**
     * set the panel with given id as activated.
     */
    @Parameter(value = "0", allowNull = false, required = true)
    private String activeTabNum;
    @Parameter(value = "", defaultPrefix = BindingConstants.LITERAL, name = "class")
    @Property
    private String css;
    @Parameter(value = "", defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String style;
    @Property(write = false)
    private String tabTitle;
    @Property(write = false)
    private Integer tabId;
    @Property(write = false)
    private String tabContentId;
    @Environmental
    private JavaScriptSupport javascriptSupport;
    private String assignedClientId;
    @Inject
    private ComponentResources resources;
    @Component
    private Zone tabContent;

    void setupRender() {
        assignedClientId = javascriptSupport.allocateClientId(clientId);
        tabContentId = javascriptSupport.allocateClientId("tabContent");
        tabId = -1;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
        tabId++;
    }

    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    void afterRender(MarkupWriter writer) {
        JSONObject spec = new JSONObject();
        spec.put("id", getClientId());
        spec.put("activeTabId", getClientId() + "_" + activeTabNum);
        javascriptSupport.addInitializerCall("oriTabControl", spec);
    }

    public String getTabStyle() {
        return tabId.toString().equalsIgnoreCase(activeTabNum) ? "ui-tabs-selected ui-state-active" : "";
    }

    /**
     * Returns a unique id for the element. This value will be unique for any
     * given rendering of a page. This value is intended for use as the id
     * attribute of the client-side element, and will be used with any
     * DHTML/Ajax related JavaScript.
     */
    public String getClientId() {
        return assignedClientId;
    }

    public Object onSelect(int tab) {
        final Object[] res = new Object[1];
        resources.triggerEvent("tabSelect", new Object[]{tab}, new ComponentEventCallback() {

            @Override
            public boolean handleResult(Object result) {
                res[1] = result;
                return result != null;
            }
        });
        return res[0] == null ? tabContent.getBody() : res[0];
    }
}
