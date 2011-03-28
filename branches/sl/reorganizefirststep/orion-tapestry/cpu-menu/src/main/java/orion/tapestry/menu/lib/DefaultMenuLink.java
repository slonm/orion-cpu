/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

import orion.tapestry.menu.pages.Navigator;

/**
 * @author Gennadiy Dobrovolsky
 */
public class DefaultMenuLink extends PageMenuLink {

    /**
     * Если пункт меню не имеет своей ссылки,
     * то этому пункту меню присваиваются ссылка
     * на страницу pageClass
     */
    private static Class<?> navigatorPageClass = Navigator.class;
    private String path;

    public DefaultMenuLink(String path) {
        // set persistent context
        Object[] o = new Object[1];
        o[0] = path;
        this.setContextPersistent(o);
        this.pageClass=navigatorPageClass;
    }

    public static void setNavigatorPageClass(Class<?> _class) {
        if (_class != null) {
            navigatorPageClass = _class;
        }
    }
}
