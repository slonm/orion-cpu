package orion.tapestry.menu.components;

import java.util.ArrayList;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import orion.tapestry.menu.lib.MenuData;

/**
 * Layout component for pages of application mavenproject1.
 */
@IncludeStylesheet({"layout.css"})
public class Layout {

    /** 
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    /**
     * Page navigation menu
     */
    @Parameter(name = "menudata", required = false, autoconnect = true)
    private ArrayList<MenuData> menudata;

    public ArrayList<MenuData> getCpuMenu() {
        return this.menudata;
    }

    boolean beginRender(MarkupWriter writer) {


        return true;
    }

}
