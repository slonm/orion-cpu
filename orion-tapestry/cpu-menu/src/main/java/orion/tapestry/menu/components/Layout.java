package orion.tapestry.menu.components;

import java.util.ArrayList;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.BindingConstants;
import orion.tapestry.menu.lib.MenuData;

/**
 * Layout component for pages of application mavenproject1.
 */
//@IncludeStylesheet({"context:layout/layout.css"})
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

//
//    @Property
//    private String pageName;
//
//
//
//    @Property
//    @Parameter(defaultPrefix = BindingConstants.LITERAL)
//    private String sidebarTitle;
//
//    @Property
//    @Parameter(defaultPrefix = BindingConstants.LITERAL)
//    private Block sidebar;
//
//    @Inject
//    private ComponentResources resources;
//
//    @Inject
//    private PageRenderLinkSource pageRenderLinkSource;
    //private Map<String, LinkMenuItem> menuLinks = new HashMap<String, LinkMenuItem>();
//    /**
//     * Use service to generate main menu
//     */  //
//    @InjectService("cpuMainMenu")
//    private CpuMenu cpuMainMenu;
//    /**
//     * get main menu
//     * @return main page menu
//     */
//    public ArrayList<MenuData> getCpuMainMenu()
//    {
//        return new ArrayList<MenuData>();
//        //return  cpuMainMenu.getMenu(this.resources);
//    }

    /*
    static class LinkMenuItem {

    public LinkMenuItem(String pageName, String activationContext) {
    this.pageName = pageName;
    this.activationContext = activationContext;
    }
    private String pageName;
    private String activationContext;

    public String getActivationContext() {
    return activationContext;
    }

    public void setActivationContext(String activationContext) {
    this.activationContext = activationContext;
    }

    public String getPageName() {
    return pageName;
    }

    public void setPageName(String pageName) {
    this.pageName = pageName;
    }
    }
     */
    boolean beginRender(MarkupWriter writer) {


        return true;
    }
//    public String getClassForPageName() {
//        return resources.getPageName().equalsIgnoreCase(pageName)
//                ? "current_page_item"
//                : null;
//    }

    /*
    public Object[] getPageNames() {
    return menuLinks.keySet().toArray();
    }

    public String getPageLink() {
    return menuLinks.get(pageName).getPageName();
    }

    public String getPageContext() {
    return menuLinks.get(pageName).getActivationContext();
    }
     */
}
