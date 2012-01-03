/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.tapestry.menu.lib;

/**
 * @author Gennadiy Dobrovolsky
 */
public class DefaultMenuLink extends PageMenuLink {

    /**
     * Если пункт меню не имеет своей ссылки,
     * то этому пункту меню присваиваются ссылка
     * на страницу pageClass
     */
    private static String navigatorPage;
    private String path;

    public DefaultMenuLink(String path) {
        // set persistent context
        Object[] o = new Object[1];
        o[0] = path;
        this.setContextPersistent(o);
        this.page = navigatorPage;
    }

    public static void setNavigatorPage(String page) {
        navigatorPage = page;
    }
}
