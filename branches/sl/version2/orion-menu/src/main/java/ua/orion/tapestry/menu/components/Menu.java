/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.components;

import java.util.PriorityQueue;
import org.apache.tapestry5.annotations.Import;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.lib.MenuItem;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
//TODO Может стоит вынести алгоритм формирования подписи в отдельный сервис?
@Import(stylesheet={"menu.css"}, library={"topmenu-classic-prototype.js"})
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
    @Inject
    private TypeCoercer coercer;
    /**
     * Localized messages
     */
    @Inject
    private Messages messages;

    //TODO Debug it
    public static String localize(String msg, IMenuLink lnk, Messages messages, TypeCoercer coercer) {
        //Пример: menu>Start где Start - подпись пункта
        String key = "menu>" + msg;
        if (messages.contains(key)) {
            return messages.get(key);
        }

        //Пример: foo.Bar где foo.Bar - имя класса сущности, с которой работает страница
        String clazzName;
        try {
            clazzName = "entity." + coercer.coerce(lnk, Class.class).getSimpleName();
            if (messages.contains(clazzName)) {
                return messages.get(clazzName);
            }
        } catch (Throwable t) {
        }

        //Пример: foo.Bar где foo.Bar - имя класса страницы
        try {
            clazzName = "page." + lnk.getPage();
            if (messages.contains(clazzName)) {
                return messages.get(clazzName);
            }
        } catch (Throwable t) {
        }
        return messages.get(key);
    }

    /**
     * @param msg - unique message name
     * @return message in current language
     */
    public String localize(String msg, IMenuLink lnk) {
        return localize(msg, lnk, messages, coercer);
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
