package orion.cpu.views.tapestry.components;

import java.util.ArrayList;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.services.CpuMenu;

/**
 * Layout component for pages of application mavenproject1.
 */
@IncludeStylesheet({"classpath:orion/cpu/views/tapestry/components/layout/layout.css"})
@SuppressWarnings("unused")
public class Layout {

    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Inject
    private CpuMenu cpuMenu;

    /**
     * Page navigation menu
     */
    @Parameter(required=true)
    @Property(write = false)
    private ArrayList<MenuData> menudata;

    ArrayList<MenuData> defaultMenudata(){
        return cpuMenu.getMenu("Start");
    }   
}
