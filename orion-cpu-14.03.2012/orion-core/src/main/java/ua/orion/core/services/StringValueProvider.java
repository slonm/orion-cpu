/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.services;

/**
 *
 * @author sl
 */
public interface StringValueProvider<T> {

    String getStringValue(T entity);
}
