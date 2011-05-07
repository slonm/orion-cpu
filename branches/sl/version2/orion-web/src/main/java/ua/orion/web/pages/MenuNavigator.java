/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.pages;

import java.util.ArrayList;
import java.util.PriorityQueue;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import ua.orion.tapestry.menu.components.Menu;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.lib.MenuItem;
import ua.orion.tapestry.menu.services.OrionMenuService;


/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuNavigator {

    private String path;
    private ArrayList<MenuData> menu;
    private MenuData lastMenu;
    @Inject
    private OrionMenuService cpuMenu;
    @Property
    private MenuItem _Item;

    @Inject
    private TypeCoercer coercer;

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
        return Menu.localize(msg, _Item.getItemLink(), messages, coercer);
    }

    void onActivate(String position) {
        this.path = position;
        Object[] context = null;
        //Logger logger = LoggerFactory.getLogger(MenuNavigator.class);
        //logger.info(path);
        this.menu = cpuMenu.getMenu(path, null, null, null);
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
