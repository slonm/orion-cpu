/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.pages;

import java.util.ArrayList;
import java.util.PriorityQueue;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import orion.tapestry.menu.components.Menu;
import orion.tapestry.menu.services.CpuMenu;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class Navigator {

    private String path;
    private ArrayList<MenuData> menu;
    private MenuData lastMenu;
    @Inject
    private CpuMenu cpuMenu;

    @Inject
    private TypeCoercer coercer;

    @Property
    private MenuItem _Item;

    /**
     * Localized messages
     */
    @Inject
    private Messages messages;

    public String getTitle() {
        return messages.get("menu>"+lastMenu.getTitle());
    }

    public PriorityQueue<MenuItem> getItems() {
        return lastMenu.getItems();
    }

    public ArrayList<MenuData> getCpuMenu() {
        return menu;
    }

    /**
     * @param msg - unique message name
     * @return message in current language
     */
    public String Localize(String msg) {
        return Menu.Localize(msg, _Item, messages, coercer);
    }

    void onActivate(String position) {
        this.path = position;
        Object[] context = null;
        //Logger logger = LoggerFactory.getLogger(Navigator.class);
        //logger.info(path);
        this.menu = cpuMenu.getMenu(path, null,null,null);
        lastMenu = menu.get(menu.size() - 1);
    }

    String onPassivate() {
        return path;
    }
}
