/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.RequestFilter;

/**
 * Хранит последнюю запрошенную страницу
 * @author slobodyanuk
 */
public interface LastPageHolder extends RequestFilter{

    Link getLastPage();
}
