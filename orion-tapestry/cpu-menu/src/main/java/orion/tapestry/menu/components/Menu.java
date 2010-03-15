/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.components;

import java.util.PriorityQueue;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
@IncludeStylesheet({"menu.css"})
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
        //return "loc:"+msg;
        return messages.get(msg);
    }

    /**
     * Menu items
     */
    public PriorityQueue<MenuItem> getItems() {
        return menudata.getItems();
    }

    /**
     * Menu title
     */
    public String getTitle() {
        return menudata.getTitle();
    }

    /**
     * Link attached to menu title
     */
    public Link getLink() {
        return menudata.getPageLink();
    }

    /**
     * Menu subitems
     */
    public boolean getSubitems() {
        return (menudata.getItems().size() > 0);
    }

}
