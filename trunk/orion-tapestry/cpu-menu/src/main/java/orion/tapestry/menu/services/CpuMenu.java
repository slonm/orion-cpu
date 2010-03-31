/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.services;

import java.util.ArrayList;
import java.util.Map;
// import org.apache.tapestry5.EventContext;
import orion.tapestry.menu.lib.MenuData;

/**
 * @author Gennadiy Dobrovolsky
 */
public interface CpuMenu {

    /**
     * Get menu tree
     * @param path
     * @param context is page context
     * @param parameters variable part of the URL parameters
     * @param anchor URL anchor
     * @return collection of menu
     */
    public ArrayList<MenuData> getMenu(String path, Object[] context,Map<String,String>parameters,String anchor);


    /**
     * Get one menu
     * @param path
     * @param context is page context
     * @param parameters variable part of the URL parameters
     * @param anchor URL anchor
     * @return one menu
     */
    public MenuData getOneMenu(String path, Object[] context,Map<String,String>parameters,String anchor);
}
