/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import org.apache.tapestry5.Link;

/**
 * @author Gennadiy Dobrovolsky
 * Mandatory parameter is path
 */
public class MenuItem implements Comparable<MenuItem> {

    /**
     * Default menu item path
     */
    static final String default_path = "Start";

    /**
     * Visible text of the menu item and position of the menu item.
     * path should have format like 
     * 001-Start>001-PersonnelManagement>004-Persons>001-PersonProfile
     */
    private String path = default_path;

    /**
     * The splitted path string
     */
    private String[] pathSplitted = {default_path};

    /**
     * Link class. itemLink can be action link or page link
     */
    private Link itemLink;

    /**
     * Page context. Parameters passed to page in URL string.
     */
    private Object[] pageContext;

    /**
     * Menu item label, visible text of the menu item
     */
    private String label;

    /**
     * Token to separate items in the menu path
     */
    public static final String token = ">";

    /**
     * Create menu item using Link object
     * @param _path visible text
     * @param _itemLink 
     */
    public MenuItem(String _path, Link _itemLink) {
        this.setPath(_path);
        this.setItemLink(_itemLink);
    }

    /**
     * Used to order the menu items
     */
    @Override
    public int compareTo(MenuItem o) {
        return this.path.compareTo(o.getPath());
    }

    public String[] getPathSplitted() {
        return pathSplitted;
    }

    public void setPathSplitted(String[] s) {
        StringBuffer sb = new StringBuffer(s[0]);
        int i, cnt = s.length;
        for (i = 1; i < cnt; i++) {
            sb.append(MenuItem.token);
            sb.append(s[i]);
        }
        path = sb.toString();
        pathSplitted = s;
        this.setLabel(s[s.length - 1].replaceFirst("^[0-9]+", ""));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String s) {
        path = s;
        pathSplitted = s.split(MenuItem.token);
        this.setLabel(pathSplitted[pathSplitted.length - 1].replaceFirst("^[0-9]+", ""));
    }

    public void setItemLink(Link ln) {
        itemLink = ln;
    }

    public Link getItemLink() {
        return itemLink;
    }

    public Object[] getPageContext() {
        return pageContext;
    }

    public void setPageContext(Object[] pc) {
        this.pageContext = pc;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String _label) {
        label = _label;
    }
}
