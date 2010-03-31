/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.components;

import java.util.PriorityQueue;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;

import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.menu.lib.IMenuLink;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Gennadiy Dobrovolsky
 */
@IncludeStylesheet({"menu.css"})
@IncludeJavaScriptLibrary({"topmenu-classic-prototype.js"})
public class Menu {

    /**
     * Menu data
     */
    @Parameter(name = "menudata", required = false, autoconnect = true)
    private MenuData menudata;
    /**
     * Menu item
     * used during iterations
     */
    @Property
    private MenuItem _Item;
    /**
     * Localized messages
     */
    @Inject
    private Messages messages;

    /**
     * @param msg - unique message name
     * @return message in current language
     */
    public String Localize(String msg) {
        return messages.get("cpumenu>"+msg);
    }

    /**
     * @return Menu items
     */
    public PriorityQueue<MenuItem> getItems() {
        return menudata.getItems();
    }

    /**
     * @return Menu title
     */
    public String getTitle() {
        // Logger logger = LoggerFactory.getLogger(Menu.class);
        // logger.info(menudata + " - class not found ");
        return menudata.getTitle();
    }

    /**
     * @return Link attached to menu title
     */
    public IMenuLink getMenuLink() {
        return menudata.getPageLink();
    }

    /**
     * Menu subitems
     * @return
     */
    public boolean getSubitems() {
        return (menudata.getItems().size() > 0);
    }
}
