/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedSet;

import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.tapestry5.EventContext;
import orion.tapestry.menu.lib.EventContextEncoder;
import orion.tapestry.menu.lib.LinkCreator;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.lib.MenuItem;
import orion.tapestry.menu.lib.MenuItemSource;

/**
 * @author Gennadiy Dobrovolsky
 */
public class CpuMenuImpl implements CpuMenu {

    TreeMap<String, MenuItemSource> configuration;
    private final DefaultLinkCreatorFactory linkFactory;

    public CpuMenuImpl(Map<String, LinkCreator> config, DefaultLinkCreatorFactory linkFactory) {
        this.configuration = new TreeMap<String, MenuItemSource>();
        this.linkFactory = linkFactory;
        for (String path : config.keySet()) {
            this.configuration.put(path, new MenuItemSource(path, config.get(path)));
        }
    }

    @Override
    public ArrayList<MenuData> getMenu(String path, Object... context) {

        ArrayList<MenuData> menuList = new ArrayList<MenuData>();

        // path as array
        String[] a = path.split(MenuItem.token);
        int deep = a.length;

        // temporary variables
        StringBuffer sb = new StringBuffer("");
        String key1, token = "", key3;
        int i, j, k;
        MenuData menu;
        MenuItemSource mi;

        SortedSet<String> subitems;
        for (i = 0; i < deep; i++) {

            // next path
            sb.append(token);
            sb.append(a[i]);
            token = MenuItem.token;
            key1 = sb.toString();
            k = key1.length();

            // menu start
            if (configuration.containsKey(key1)) {
                mi = configuration.get(key1);
            } else {
                mi = new MenuItemSource(key1, linkFactory.create(key1));
            }
            menu = new MenuData(mi.createMenuItem(context), new PriorityQueue<MenuItem>());

            // --------------- get keys of subitems - begin --------------------
            subitems = new TreeSet<String>();
            key3 = key1;
            while ((key3 = configuration.higherKey(key3)) != null) {
                // if start of the path
                if (!key3.startsWith(key1 + MenuItem.token)) {
                    break;
                }
                j = key3.indexOf(MenuItem.token, k + 1);
                subitems.add(key3.substring(0, (j > 0) ? j : key3.length()));
            }
            // --------------- get keys of subitems - end ----------------------

            // --------------- create subitems - begin -------------------------
            for (String it : subitems) {
                if (configuration.containsKey(it)) {
                    mi = configuration.get(it);
                } else {
                    mi = new MenuItemSource(it, linkFactory.create(it));
                }
                menu.addItem(mi.createMenuItem(context));
            }
            // --------------- create subitems - end ---------------------------

            // add item to menu
            menuList.add(menu);
        }
        return menuList;
    }

    @Override
    public ArrayList<MenuData> getMenu(String path, EventContext context) {
        return this.getMenu(path, EventContextEncoder.toObjectArray(context));
    }

    @Override
    public MenuData getOneMenu(String path, EventContext context) {
        return this.getOneMenu(path, EventContextEncoder.toObjectArray(context));
    }

    @Override
    public MenuData getOneMenu(String path, Object... context) {
        MenuItemSource mi;
        // menu start
        if (configuration.containsKey(path)) {
            mi = configuration.get(path);
        } else {
            mi = new MenuItemSource(path, linkFactory.create(path));
        }
        MenuData menu = new MenuData(mi.createMenuItem(context), new PriorityQueue<MenuItem>());


        String key3;
        int i, j, k;
        SortedSet<String> subitems;
        // --------------- get keys of subitems - begin ------------------------
        subitems = new TreeSet<String>();
        key3 = path;
        k = path.length();
        while ((key3 = configuration.higherKey(key3)) != null) {
            // if start of the path
            if (!key3.startsWith(path + MenuItem.token)) {
                break;
            }
            j = key3.indexOf(MenuItem.token, k + 1);
            subitems.add(key3.substring(0, (j > 0) ? j : key3.length()));
        }
        // --------------- get keys of subitems - end --------------------------

        // --------------- create subitems - begin -----------------------------
        for (String it : subitems) {
            if (configuration.containsKey(it)) {
                mi = configuration.get(it);
            } else {
                mi = new MenuItemSource(it, linkFactory.create(it));
            }
            menu.addItem(mi.createMenuItem(context));
        }
        // --------------- create subitems - end -------------------------------
        return menu;
    }
}
