package orion.tapestry.grid.services;

import orion.tapestry.grid.lib.datasource.DataSource;

/**
 * Источник данных для Grid
 * @author dobro
 */
public interface CpuGridDataSourceFactory {
    public DataSource createDataSource(Class entityClass);
}
