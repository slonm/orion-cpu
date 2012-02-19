/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.tapestry.menu.lib;

import org.apache.tapestry5.Link;

/**
 * @author Gennadiy Dobrovolsky
 * Интерфейс фабрики объектов, имеющих тип org.apache.tapestry5.Link .
 * Используется при составлении навигационного меню.
 */
public interface LinkCreator {
    /**
     * creates Link object
     * @param context - page context parameters
     * @return Link for item of navigation menu
     */
    public Link create(Object ... context);
}
