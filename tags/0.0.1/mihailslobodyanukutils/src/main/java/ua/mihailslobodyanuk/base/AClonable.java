/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.mihailslobodyanuk.base;

/**
 *
 * @author sl
 */
public interface AClonable<T> extends Cloneable{
    T clone();
}
