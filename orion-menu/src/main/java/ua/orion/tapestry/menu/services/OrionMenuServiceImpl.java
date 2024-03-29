/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;

import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.AssetSource;
import ua.orion.tapestry.menu.lib.DefaultMenuLink;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.tapestry.menu.lib.MenuData;
import ua.orion.tapestry.menu.lib.MenuItem;
import ua.orion.tapestry.menu.lib.MenuItemPosition;
import ua.orion.tapestry.menu.lib.MenuItemSource;

/**
 * Класс представляет структуру данных для отображения навигационного меню
 *
 * @author Gennadiy Dobrovolsky
 */
public class OrionMenuServiceImpl implements OrionMenuService {

    //private Logger logger;
    public SortedMap<MenuItemPosition, MenuItemSource> fullMenu;
    private final TypeCoercer coercer;
    private final AssetSource as;

    public OrionMenuServiceImpl(Map<String, IMenuLink> config, TypeCoercer coercer, AssetSource as) {
        this.coercer = coercer;
        this.as = as;
        //logger=LoggerFactory.getLogger(CpuMenuModule.class);

        ArrayList<MenuItemSource> tmp = new ArrayList<MenuItemSource>();

        // ---------- create items  - begin ------------------------------------
        // create MenuItemSource() list
        for (String path : config.keySet()) {
            //logger.debug(">>>>>>>>>>>>"+path+"  "+config.get(path));
            //System.out.println(">>>>>>>>>>>>"+path+"  "+config.get(path));
            tmp.add(new MenuItemSource(path, config.get(path)));
        }
        // ---------- create items  - end --------------------------------------

        // ---------- get existing positions - begin ---------------------------
        Set<String> existing_positions = new TreeSet<String>();
        for (String path : config.keySet()) {
            MenuItemPosition mp = new MenuItemPosition(path);
            existing_positions.add(mp.uid);
            //System.out.println("Existing:"+mp.uid);
        }
        // ---------- get existing positions - end -----------------------------


        // ---------- add missing items - begin --------------------------------
        //parents;
        Set<String> missing_positions = new TreeSet<String>();
        for (MenuItemSource mis : tmp) {
            ArrayList<MenuItemPosition> parents = mis.position.getParents();
            for (MenuItemPosition mp : parents) {
                if (!existing_positions.contains(mp.uid) && !missing_positions.contains(mp.uid)) {
                    missing_positions.add(mp.uid);
                }
            }
        }
        //System.out.println("Size:"+missing_positions.size());
        for (String mp : missing_positions) {
            tmp.add(new MenuItemSource(mp, new DefaultMenuLink(mp)));
            //System.out.println("Creating:"+mp);
        }
        // ---------- add missing items - end ----------------------------------

        // ---------- update positions - begin ---------------------------------
        int w, cnt;
        for (MenuItemSource mis : tmp) {
            //System.out.println("Checking:"+mis.position.uid);
            w = mis.position.getLastWeight();
            cnt = mis.position.positionSplitted.length;
            if (w > 0) {
                // ---------- update weights - begin ---------------------------
                for (MenuItemSource upd : tmp) {
                    if (upd != mis && upd.position.isChildOf(mis.position)) {
                        //System.out.println("Updating:"+upd.position.position);
                        upd.position.updatePositionWeight(cnt - 1, w);
                        //System.out.println("Updating:"+upd.position.position);
                        //System.out.println("-------------");
                    }
                }
                // ---------- update weights - end -----------------------------
            }
        }
        // ---------- update positions - end -----------------------------------

        // ---------- create full menu tree - begin ----------------------------
        this.fullMenu = new TreeMap<MenuItemPosition, MenuItemSource>();
        for (MenuItemSource mis : tmp) {
            this.fullMenu.put(mis.position, mis);
        }
        // ---------- create full menu tree - end ------------------------------

    }

    /**
     * @param mp position to search for
     * @return true if the menu position mp exists in the menu tree
     */
    public boolean containsKey(MenuItemPosition mp) {
        for (MenuItemPosition tmp : this.fullMenu.keySet()) {
            if (mp.equals(tmp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param mp position to search for
     * @return menu item source at given position
     */
    public MenuItemSource get(MenuItemPosition mp) {
        for (MenuItemPosition tmp : this.fullMenu.keySet()) {
            if (mp.equals(tmp)) {
                return this.fullMenu.get(tmp);
            }
        }
        return null;
    }

    /**
     * Create one menu (i.e. orion.tapestry.menu.lib.MenuData object )
     *
     * @param path position ot the menu
     * @param context additional paremeters
     * @param parameters
     * @param anchor
     * @return one menu
     * @see orion.tapestry.menu.lib.MenuData
     */
    // ComponentResources LinkSource,
    public MenuData getOneMenu(MenuItemPosition path, Object[] context, Map<String, String> parameters, String anchor) {

        if (!containsKey(path)) {
            return null;
        }

        MenuItemSource mis;
        mis = this.get(path);
        MenuData menu = new MenuData(mis.createMenuItem(context, parameters, anchor), new PriorityQueue<MenuItem>());

        // --------------- create subitems - begin -----------------------------
        int deep = path.positionSplitted.length;
        for (MenuItemPosition tmp : this.fullMenu.keySet()) {
            if (tmp.isChildOf(path) && tmp.positionSplitted.length - 1 == deep) {
                mis = this.fullMenu.get(tmp);
                menu.addItem(mis.createMenuItem(context, parameters, anchor));
            }
        }
        // --------------- create subitems - end -------------------------------
        return menu;
    }

    @Override
    public MenuData getOneMenu(String path, Object[] context, Map<String, String> parameters, String anchor) {
        return getOneMenu(new MenuItemPosition(path), context, parameters, anchor);
    }

    /**
     * Get menu bar for current position
     *
     * @param path position ot the last menu item (
     * orion.tapestry.menu.lib.MenuItemPosition )
     * @param context additional link parameters
     * @return collection of the menus
     * @see orion.tapestry.menu.lib.MenuItemPosition
     */
    public ArrayList<MenuData> getMenu(MenuItemPosition path, Object[] context, Map<String, String> parameters, String anchor) {

        ArrayList<MenuData> menuList = new ArrayList<MenuData>();

        ArrayList<MenuItemPosition> parents = path.getParents();

        MenuData med;
        for (MenuItemPosition _mp : parents) {
            med = getOneMenu(_mp, context, parameters, anchor);
            if (med != null) {
                menuList.add(med);
            }
        }
        med = getOneMenu(path, context, parameters, anchor);
        if (med != null) {
            menuList.add(med);
        }
        return menuList;
    }

    @Override
    public ArrayList<MenuData> getMenu(String path, Object[] context, Map<String, String> parameters, String anchor) {
        return getMenu(new MenuItemPosition(path), context, parameters, anchor);
    }

    /**
     * Создание иконок для сущностей
     *
     * @param msg
     * @param lnk
     * @param messages
     * @param size - размер иконки (big или small)
     * @return URL иконки
     */
    public String createIcon(String msg, IMenuLink lnk, Messages messages, String size) {
        String[] menuParts = msg.split(">");
        String appVersion = "0.0.3-SNAPSHOT";
        String clazzName = menuParts[menuParts.length - 1];
        try {
            as.getClasspathAsset("ua/orion/web/images/icons/" + size + "/" + clazzName + ".png");
        } catch (Exception e) {
            return "/assets/" + appVersion + "/ori/images/icons/" + size + "/Folder.png";
        }
        return "/assets/" + appVersion + "/ori/images/icons/" + size + "/" + clazzName + ".png";
    }

    /**
     * Создание маленькой иконки
     * @param msg
     * @param lnk
     * @param messages
     * @return 
     */
    public String createSmallIcon(String msg, IMenuLink lnk, Messages messages) {
        return createIcon(msg, lnk, messages, "small");
    }

    /**
     * Создание большой иконки
     * @param msg
     * @param lnk
     * @param messages
     * @return 
     */
    public String createBigIcon(String msg, IMenuLink lnk, Messages messages) {
        return createIcon(msg, lnk, messages, "big");
    }

    public String localizeItem(String msg, IMenuLink lnk, Messages messages) {

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
        } catch (Exception t) {
        }

        //Пример: foo.Bar где foo.Bar - имя класса страницы
        try {
            clazzName = "page." + lnk.getPage();
            if (messages.contains(clazzName)) {
                return messages.get(clazzName);
            }
        } catch (Exception t) {
        }
        return messages.get(key);
    }
}
