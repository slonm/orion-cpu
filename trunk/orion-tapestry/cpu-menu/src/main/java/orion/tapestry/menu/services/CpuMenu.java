/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.services;

import java.util.ArrayList;
import org.apache.tapestry5.EventContext;
import orion.tapestry.menu.lib.MenuData;

/**
 * @author Gennadiy Dobrovolsky
 */
public interface CpuMenu {

    /**
     * Get menu tree
     * @param path
     * @param context is page context
     * @return menu
     */
    public ArrayList<MenuData> getMenu(String path, Object ... context);

    /**
     * Get menu tree
     * @param path
     * @param context is page context
     * @return menu
     */
    public ArrayList<MenuData> getMenu(String path, EventContext context);

    /**
     * Get one menu
     * @param path
     * @param context is page context
     * @return menu
     */
    public MenuData getOneMenu(String path, EventContext context);

    /**
     * Get one menu
     * @param path
     * @param context is page context
     * @return menu
     */
    public MenuData getOneMenu(String path, Object ... context);
}
