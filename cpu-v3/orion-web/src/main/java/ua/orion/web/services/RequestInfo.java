/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.RequestFilter;

/**
 * Информация о Http запросе
 * @author slobodyanuk
 */
public interface RequestInfo extends RequestFilter {

    /**
     * Возвращает последнюю запрошенную страницу
     */
    Link getLastPage();

    /**
     * возвращает true если обрабатывается http request события компонента 
     * и false, если  обрабатывается http request отрисовки страницы
     */
    boolean isComponentEventRequest();
}
