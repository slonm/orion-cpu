package test.menu.pages;

//import org.apache.tapestry5.ComponentResources;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import orion.tapestry.menu.lib.MenuData;
import orion.tapestry.menu.services.CpuMenu;

/**
 * Стартовая страница информационной системы КПУ.
 * Навигация по другим страницам описана в ./components/Layout.java
 */
public class Index {

    @Inject
    private PersistentLocale persistentLocale;

    {
        persistentLocale.set(new Locale("ru"));
    }

    @Inject
    private CpuMenu cpuMenu;
    public ArrayList<MenuData> getCpuMenu() {
        //Object[] context = null;
        ArrayList<MenuData> menu = cpuMenu.getMenu("Start>abo", null,null,null);
        return menu;
    }

    public MenuData getContextMenu(int someid) {
        //Object[] context = null;
        MenuData menu = cpuMenu.getOneMenu("Start>abo",  null,null,null);
        return menu;
    }
}
