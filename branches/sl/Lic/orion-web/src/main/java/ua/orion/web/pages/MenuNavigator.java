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
    public String localize(String msg) {
        return cpuMenu.localizeItem(msg, _Item.getItemLink(), messages);
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
    
        /**
     * Получение иконки
     * @return css-свойство устанавливающее иконку как background-image
     */
    public String getUidIcon() {
        String iconURL = messages.get(_Item.getUid() + "-icon");
        if (iconURL.indexOf("missing key") < 1) {
            return "background-image: url('/webcontent/e-icons/" + iconURL + "')";
        } else {
            String[] menuParts = _Item.getUid().split(">");
            String lastMenuPart = menuParts[menuParts.length - 1];
            iconURL = messages.get(lastMenuPart + "-icon");
            if (iconURL.indexOf("missing key") < 1) {
                return "background-image: url('/webcontent/e-icons/" + iconURL + "')";
            }
        }
        return "background-image: url('/webcontent/e-icons/folder.png')";
    }
}
