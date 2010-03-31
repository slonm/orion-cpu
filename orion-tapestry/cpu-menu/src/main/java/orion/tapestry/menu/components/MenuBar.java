package orion.tapestry.menu.components;

import java.util.ArrayList;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import orion.tapestry.menu.lib.MenuData;

/**
 * @author Gennadiy Dobrovolsky
 * Компонета для рисования строки меню
 */


public class MenuBar {

    @Parameter(name = "menudata", required = false, autoconnect = true)
    private ArrayList<MenuData> menudata;

    public ArrayList<MenuData> getMenuList() {
        return menudata;
    }

    ;
    @Property
    private MenuData _Menu;
}
