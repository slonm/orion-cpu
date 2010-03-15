/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.views.tapestry.pages;

import java.util.ArrayList;
import java.util.PriorityQueue;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.menu.services.CpuMenu;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuNavigator {

    private String path;
    private ArrayList<MenuData> menu;
    private MenuData lastMenu;
    @Inject
    private CpuMenu cpuMenu;
    @Property
    private MenuItem _Item;

    /**
     * Localized messages
     */
    @Inject
    private Messages messages;

    public String getTitle() {
        return messages.get(lastMenu.getTitle());
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
        //return "loc:"+msg;
        return messages.get(msg);
    }

    void onActivate(String position) {
        this.path = position;
        Object[] context = null;
        //Logger logger = LoggerFactory.getLogger(MenuNavigator.class);
        //logger.info(path);
        this.menu = cpuMenu.getMenu(path, context);
        lastMenu = menu.get(menu.size() - 1);
    }

//    Выполняются ВСЕ методы с именем onActivate
//    так что
//    void onActivate() {
//        onActivate("Start");
//    }

    String onPassivate() {
        return path;
    }
}
