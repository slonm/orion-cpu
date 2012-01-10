
package ua.orion.tapestry.menu.lib;

import java.util.Map;

/**
 * @author Gennadiy Dobrovolsky
 * Фабрика пунктов меню.
 */
public class MenuItemSource implements Comparable<MenuItemSource> {

    /**
     * The position of the menu item
     */
    public MenuItemPosition position;

    /**
     * Link class. itemLink can be action link or page link
     */
    private IMenuLink itemLink;

    /**
     * Menu item label, visible text of the menu item
     */
    private String label;

    /**
     * Create menu item source using Link object
     * @param _path visible text
     * @param _itemLink 
     */
    public MenuItemSource(String _path, IMenuLink _itemLink) {
        this.position = new MenuItemPosition(_path);
        this.setItemLink(_itemLink);
        this.label=this.position.getLastLabel();
    }

    /**
     * Used to order the menu items
     */
    @Override
    public int compareTo(MenuItemSource o) {
        return this.position.position.compareTo(o.position.position);
    }

    /**
     * @param context
     * @param parameters 
     * @param anchor
     * @return menu item
     */
    public MenuItem createMenuItem(Object[] context, Map<String,String> parameters, String anchor){
        if(context!=null){
           itemLink.setContextVariable(context);
        }
        if(parameters!=null){
           itemLink.setParameterVariable(parameters);
        }
        itemLink.setAnchor(anchor);
        MenuItem mi=new MenuItem(position,itemLink);
        return mi;
    }


    public void setItemLink(IMenuLink ln) {
        itemLink = ln;
    }

    public IMenuLink getItemLink() {
        return itemLink;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String _label) {
        label = _label;
    }
}
