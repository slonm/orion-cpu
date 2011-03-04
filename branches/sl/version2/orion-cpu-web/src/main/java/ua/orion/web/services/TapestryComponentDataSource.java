/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.orion.web.services;

import org.apache.tapestry5.grid.GridDataSource;

/**
 *
 * @author slobodyanuk
 */
public interface TapestryComponentDataSource {
    GridDataSource getGridDataSource(Class<?> entityClass);
    
}
