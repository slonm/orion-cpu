/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.grid.services;

import orion.tapestry.grid.lib.datasource.DataSource;

/**
 *
 * @author dobro
 */
public interface CpuGridDataSourceFactory {
    public DataSource createDataSource(Class entityClass);
}
