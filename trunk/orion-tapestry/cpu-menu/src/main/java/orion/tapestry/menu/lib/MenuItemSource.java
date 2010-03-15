
package orion.tapestry.menu.lib;

/**
 * @author Gennadiy Dobrovolsky
 * Фабрика пунктов меню.
 */
public class MenuItemSource implements Comparable<MenuItemSource> {

    /**
     * Default menu item path
     */
    static final String default_path = "Start";

    /**
     * Visible text of the menu item and position of the menu item.
     * Path should have format like
     * <p><code>001Start>001PersonnelManagement>004Persons>001PersonProfile</code></p>
     * The symbol ">" separates entities in the path
     * Each entity can start with some number.
     * The number is used to sort menu items.
     */
    private String path = default_path;

    /**
     * The splitted path string
     */
    private String[] pathSplitted = {default_path};

    /**
     * Link class. itemLink can be action link or page link
     */
    private LinkCreator itemLink;

    /**
     * Menu item label, visible text of the menu item
     */
    private String label;

    /**
     * Create menu item using Link object
     * @param _path visible text
     * @param _itemLink 
     */
    public MenuItemSource(String _path, LinkCreator _itemLink) {
        this.setPath(_path);
        this.setItemLink(_itemLink);
    }

    /**
     * Used to order the menu items
     */
    @Override
    public int compareTo(MenuItemSource o) {
        return this.path.compareTo(o.getPath());
    }

    /**
     * @param context
     * @return menu item
     */
    public MenuItem createMenuItem(Object ... context){
        return new MenuItem(path, itemLink.create(context));
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

    public void setItemLink(LinkCreator ln) {
        itemLink = ln;
    }

    public LinkCreator getItemLink() {
        return itemLink;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String _label) {
        label = _label;
    }
}
