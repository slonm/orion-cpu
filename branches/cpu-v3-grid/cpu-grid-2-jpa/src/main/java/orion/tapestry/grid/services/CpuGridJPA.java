package orion.tapestry.grid.services;

import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * Определяет источник данных для компоненты CpuGrid
 * @author dobro
 */
public class CpuGridJPA {
    public static void bind(ServiceBinder binder) {
        binder.bind(CpuGridDataSourceFactory.class, CpuGridDataSourceFactoryImpl.class);
    }
}
