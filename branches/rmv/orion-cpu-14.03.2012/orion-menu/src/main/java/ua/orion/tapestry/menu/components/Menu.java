/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.components;

import java.io.File;
import java.util.PriorityQueue;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.lib.MenuItem;
import ua.orion.tapestry.menu.services.OrionMenuService;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
//TODO Может стоит вынести алгоритм формирования подписи в отдельный сервис?
@Import(stylesheet = {"menu.css"}, library = {"topmenu-classic-jquery.js"})
public class Menu {

    /**
     * Menu data
     */
    @Parameter(name = "menudata", required = false, autoconnect = true)
    private MenuData menudata;
    /**
     * Menu item used during iterations
     */
    @Property
    private MenuItem _Item;
    @Inject
    private TypeCoercer coercer;
    @Inject
    private OrionMenuService menuService;
    /**
     * Localized messages
     */
    @Inject
    private Messages messages;

    /**
     * @param msg - unique message name
     * @return message in current language
     */
    public String localize(String msg, IMenuLink lnk) {
        return menuService.localizeItem(msg, lnk, messages);
    }

    /**
     * Получение маленькой иконки в качестве background
     */
    public String prepareSmallIconInStyleAttribute(String msg, IMenuLink lnk) {
        return "background: url('" + getSmallIcon(msg, lnk) + "')";
    }

    /**
     * Получение маленькой иконки в качестве background
     */
    public String getSmallIcon(String msg, IMenuLink lnk) {
        return menuService.createSmallIcon(msg, lnk, messages);
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
     *
     * @return
     */
    public boolean getSubitems() {
        return (menudata.getItems().size() > 0);
    }
    /**
     * Получение иконки
     *
     * @return css-свойство устанавливающее иконку как background-image
     */
}
