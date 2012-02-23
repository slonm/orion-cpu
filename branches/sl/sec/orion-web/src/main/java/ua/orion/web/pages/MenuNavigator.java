/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.pages;

import java.util.ArrayList;
import java.util.PriorityQueue;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.tapestry.menu.lib.IMenuLink;
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
    /**
     * Localized messages
     */
    @Inject
    private Messages messages;

    public String getTitle() {
        return messages.get("menu>" + lastMenu.getTitle());
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
    public String localize(String msg) {
        return cpuMenu.localizeItem(msg, _Item.getItemLink(), messages);
    }

    /**
     * Получение большой иконки в качестве background
     */
    public String prepareBigIconInStyleAttribute(String msg) {
        return "background: url('" + getBigIcon(msg) + "')";
    }

    /**
     * Получение большой иконки
     */
    public String getBigIcon(String msg) {
        return cpuMenu.createBigIcon(msg, _Item.getItemLink(), messages);
    }

    void onActivate(String position) {
        this.path = position;
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
