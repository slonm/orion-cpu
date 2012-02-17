/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.lib;

/**
 * @author Gennadiy Dobrovolsky
 * Mandatory parameter is path
 */
public class MenuItem implements Comparable<MenuItem> {

    /**
     * The position of the menu item
     */
    public MenuItemPosition position;

    /**
     * Link class. itemLink can be action link or page link
     */
    public IMenuLink itemLink;


    /**
     * Menu item label, visible text of the menu item
     */
    public String label;


    /**
     * Menu item uid, primary key to identify menu item
     */
    public String uid;

    /**
     * Create menu item using Link object
     * @param _path visible text
     * @param _itemLink 
     */
    public MenuItem(String _path, IMenuLink _itemLink) {
        this.position = new MenuItemPosition(_path);
        this.itemLink = _itemLink;
        this.uid=this.position.uid;
        this.label = this.position.getLastLabel();
    }

    public MenuItem(MenuItemPosition _path, IMenuLink _itemLink) {
        this.position = _path;
        this.itemLink = _itemLink;
        this.label = this.position.getLastLabel();
        this.uid=this.position.uid;
    }
    /**
     * Used to order the menu items
     */
    @Override
    public int compareTo(MenuItem o) {
        return this.position.position.compareTo(o.position.position);
    }

    public MenuItemPosition getPosition() {
        return position;
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

    public String getUid() {
        return uid;
    }
}
